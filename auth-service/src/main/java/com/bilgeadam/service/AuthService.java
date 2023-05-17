package com.bilgeadam.service;

import com.bilgeadam.rabbitmq.producer.RegisterMailProducer;
import com.bilgeadam.rabbitmq.request.ActivateRequestDto;
import com.bilgeadam.rabbitmq.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.rabbitmq.request.LoginRequestDto;
import com.bilgeadam.rabbitmq.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final IUserManager userManager;
    private final JwtTokenManager tokenManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer mailProducer;

    public AuthService(IAuthRepository authRepository, IUserManager userManager, JwtTokenManager tokenManager, CacheManager cacheManager, RegisterProducer registerProducer, RegisterMailProducer mailProducer){
        super(authRepository);
        this.authRepository=authRepository;
        this.userManager = userManager;
        this.tokenManager = tokenManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.mailProducer = mailProducer;
    }
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());

        try{
            save(auth); // auth nesnesi burada Id kazandı
            userManager.createUser(IAuthMapper.INSTANCE.toNewCreateUserRequestDto(auth));

            // ekledigim role ait olan cache'i siliyorum:
            cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        }catch (Exception e){
            delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }

        return IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
    }

    @Transactional
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());

        try{
            save(auth); // auth nesnesi burada Id kazandı

        // alttaki kısmı iptal ettik (feignClient ile haberleşiyor)
        //  userManager.createUser(IAuthMapper.INSTANCE.toNewCreateUserRequestDto(auth));

            /** rabbitMq ile haberleşme saglanacak:   */
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));
            mailProducer.sendActivationCode(IAuthMapper.INSTANCE.toRegisterMailModel(auth));

            // ekledigim role ait olan cache'i siliyorum:
        //    cacheManager.getCache("findbyrole").evict(auth.getRole().toString().toUpperCase());
        }catch (Exception e){
            // delete(auth);
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
        return IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
    }
    public String login(LoginRequestDto dto) {
        Optional<Auth> auth=authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return tokenManager.createToken(auth.get().getId(),auth.get().getRole()).orElseThrow(()-> {
            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
        } );
    }
    public Boolean activateStatus(ActivateRequestDto dto) { // dto, dışarıdan gelen
        Optional<Auth> auth=findById(dto.getId()); // auth, db'de kayıtlı olan
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(dto.getActivationCode().equals(auth.get().getActivationCode())){
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
            userManager.activateStatus(auth.get().getId());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }
    public Boolean updateEmailOrUsername(UpdateEmailOrUsernameRequestDto dto) { // dto, dışarıdan gelen
        Optional<Auth> auth=authRepository.findById(dto.getAuthId()); // auth, db'de kayıtlı olan
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setUsername(dto.getUsername());
        auth.get().setEmail(dto.getEmail());
        update(auth.get());
        return true;
    }
    @Transactional
    public Boolean delete(Long id) {
        Optional<Auth> auth=findById(id); // auth, db'de kayıtlı olan
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(id);
        return true;
    }
    @Transactional
    public Boolean delete2(String token) {
        Optional<Long> authId=tokenManager.getIdFromToken(token);
        if(authId.isEmpty()){
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> auth=findById(authId.get()); // auth, db'de kayıtlı olan
        if(auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(authId.get());
        return true;
    }

    public List<Long> findByRole(String role) {
        ERole myrole;
        try{
            // girdigimiz String degeri Enum'a çeviriyoruz:
            myrole=ERole.valueOf(role.toUpperCase(Locale.ENGLISH));
        }catch (Exception e){
            throw new AuthManagerException(ErrorType.ROLE_NOT_FOUND);
        }

        return authRepository.findAllByRole(myrole).stream().map(x->x.getId()).collect(Collectors.toList());
    }
}