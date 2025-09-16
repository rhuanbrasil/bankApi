package com.api.bankApi.business.mapstruct;

import com.api.bankApi.business.dtos.UserRegisterDto;
import com.api.bankApi.infra.entitys.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserUpdaterMapper {
    User updateUser(UserRegisterDto userRegisterDto, @MappingTarget User user);
}
