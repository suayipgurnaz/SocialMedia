package com.bilgeadam.repository;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constants.ApiUrls.CREATE;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findOptionalByAuthId(Long authid);

    Optional<UserProfile> findOptionalByUsernameIgnoreCase(String username);
}
