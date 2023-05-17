package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE= Mappers.getMapper(IUserMapper.class);

    UserProfile toUserProfile(final NewCreateUserRequestDto dto);
    UserProfile toUserProfile(final RegisterModel model);
    NewCreateUserRequestDto toNewCreateUserRequestDto(final RegisterModel model);
    UpdateEmailOrUsernameRequestDto toUpdateEmailOrUsernameRequestDto(final UserProfileUpdateRequestDto dto);
}
