package com.bilgeadam.mapper;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.rabbitmq.request.NewCreateUserRequestDto;
import com.bilgeadam.rabbitmq.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE= Mappers.getMapper(IAuthMapper.class);

    Auth toAuth(final RegisterRequestDto dto);
    RegisterResponseDto toRegisterResponseDto(final Auth auth);

    @Mapping(source = "id", target = "authId")
    NewCreateUserRequestDto toNewCreateUserRequestDto(final Auth auth);

    @Mapping(source = "id", target = "authId")
    RegisterModel toRegisterModel(final Auth auth);

    RegisterMailModel toRegisterMailModel(final Auth auth);
}
