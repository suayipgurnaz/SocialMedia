package com.bilgeadam.controller;

import com.bilgeadam.rabbitmq.request.ActivateRequestDto;
import com.bilgeadam.rabbitmq.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.rabbitmq.request.LoginRequestDto;
import com.bilgeadam.rabbitmq.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager tokenManager;
    private final CacheManager cacheManager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER+"2")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findall(){
        return ResponseEntity.ok(authService.findAll());
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(Long id){
        return ResponseEntity.ok(authService.delete(id));
    }

    @PutMapping(DELETEBYID+2)
    public ResponseEntity<Boolean> delete2(String token){
        return ResponseEntity.ok(authService.delete2(token));
    }

    @PutMapping("/updateemailorusername")
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto){
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }

    @GetMapping("/createtoken")
    public ResponseEntity<String> createToken(Long id, ERole role){
        return ResponseEntity.ok(tokenManager.createToken(id, role).get());
    }
    @GetMapping("/createtoken2")
    public ResponseEntity<String> createToken2(Long id){
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }
    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long> getIdFromToken(String token){
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/getrolefromtoken")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }

    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value){
        try{
            Thread.sleep(2000);
            return value;
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    //redisexample cache'indeki tum verileri silen method
    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample", allEntries = true) // name olarak yukarıdakinin value'sunu verdik
    public void redisDelete(){
    }

    // cache'deki  verileri silmenin 2. yolu
    @GetMapping("/redisdelete2")
    public Boolean redisDelete2(){
        try {
         //   cacheManager.getCache("redisexample").clear(); // girilen isimle cache'lenmiş tum verileri siler
            cacheManager.getCache("redisexample").evict("mustafa"); // girilen isimle cache'lenmiş "mustafa"ları siler
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>> findByRole(@RequestParam String role){ // buldugu rollerin authId'lerini donecek
        return ResponseEntity.ok(authService.findByRole(role));
    }
}
