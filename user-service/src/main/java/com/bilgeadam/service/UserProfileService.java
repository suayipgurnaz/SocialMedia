package com.bilgeadam.service;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile,Long> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    private final IAuthManager authManager;
    private final CacheManager cacheManager;

    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager, CacheManager cacheManager) {
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
    }
    public Boolean createUser(NewCreateUserRequestDto dto) {
        try{
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        }catch (Exception exception){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try{
            save(IUserMapper.INSTANCE.toUserProfile(model));
            return true;
        }catch (Exception exception){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
    public Boolean activateStatus(Long authId){
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId);
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean update(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId=jwtTokenManager.getIdFromToken(dto.getToken());
        if(authId.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId.get());
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

        // redis cache'teki veriyi siliyoruz
        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername().toLowerCase());

        if(!dto.getUsername().equals(userProfile.get().getUsername()) ||
           !dto.getEmail().equals(userProfile.get().getEmail())){

            userProfile.get().setUsername(dto.getUsername());
            userProfile.get().setEmail(dto.getEmail());

            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto=IUserMapper
                    .INSTANCE.toUpdateEmailOrUsernameRequestDto(dto);
            // updateEmailOrUsernameRequestDto'da authId şu anda boş, bunu elimizle ekliyoruz:
            updateEmailOrUsernameRequestDto.setAuthId(authId.get());
            authManager.updateEmailOrUsername(updateEmailOrUsernameRequestDto);
        }

        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());
        return true;
    }
    public Boolean delete(Long authId) {
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId);
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }
    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile  findByUsername(String username) {
        // cache olayını denemek için sleep koyuyoruz, normalde buna gerek yok!
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole", key = "#role.toUpperCase()")
    public List<UserProfile>  findByRole(String role) {
        // cache olayını denemek için sleep koyuyoruz, normalde buna gerek yok!
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        String str1 = "ankara";
//        String str = str1.toUpperCase(Locale.ENGLISH);
        // findByRole methodunun dondugu ResponseEntity<Long> degerlerini Long olarak almak için getBody() kullanıyoruz
        List<Long> authIds = authManager.findByRole(role).getBody();
//        ResponseEntity<List<Long>> authIds2=authManager.findByRole(role);

        return authIds.stream().map(x-> userProfileRepository.findOptionalByAuthId(x).orElseThrow( ()-> {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        })).collect(Collectors.toList());
    }


}










