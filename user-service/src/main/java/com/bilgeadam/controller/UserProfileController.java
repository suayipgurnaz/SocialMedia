package com.bilgeadam.controller;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }
    @GetMapping(ACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PutMapping(UPDATE)  // @PostMapping(UPDATE) de olur
    public ResponseEntity<Boolean> update(@RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long authId){
        return ResponseEntity.ok(userProfileService.delete(authId));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findall(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping("/findbyusername")
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username){
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("/findbyrole")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role){
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }
}








