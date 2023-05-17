package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-13T06:42:34+0300",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.5 (Amazon.com Inc.)"
)
@Component
public class IUserMapperImpl implements IUserMapper {

    @Override
    public UserProfile toUserProfile(NewCreateUserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( dto.getAuthId() );
        userProfile.username( dto.getUsername() );
        userProfile.email( dto.getEmail() );

        return userProfile.build();
    }

    @Override
    public UserProfile toUserProfile(RegisterModel model) {
        if ( model == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( model.getAuthId() );
        userProfile.username( model.getUsername() );
        userProfile.email( model.getEmail() );

        return userProfile.build();
    }

    @Override
    public NewCreateUserRequestDto toNewCreateUserRequestDto(RegisterModel model) {
        if ( model == null ) {
            return null;
        }

        NewCreateUserRequestDto.NewCreateUserRequestDtoBuilder newCreateUserRequestDto = NewCreateUserRequestDto.builder();

        newCreateUserRequestDto.authId( model.getAuthId() );
        newCreateUserRequestDto.username( model.getUsername() );
        newCreateUserRequestDto.email( model.getEmail() );

        return newCreateUserRequestDto.build();
    }

    @Override
    public UpdateEmailOrUsernameRequestDto toUpdateEmailOrUsernameRequestDto(UserProfileUpdateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UpdateEmailOrUsernameRequestDto.UpdateEmailOrUsernameRequestDtoBuilder updateEmailOrUsernameRequestDto = UpdateEmailOrUsernameRequestDto.builder();

        updateEmailOrUsernameRequestDto.username( dto.getUsername() );
        updateEmailOrUsernameRequestDto.email( dto.getEmail() );

        return updateEmailOrUsernameRequestDto.build();
    }
}
