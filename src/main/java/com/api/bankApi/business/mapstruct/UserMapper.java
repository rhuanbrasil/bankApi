package com.api.bankApi.business.mapstruct;

import com.api.bankApi.business.dtos.UserRegisterDto;
import com.api.bankApi.infra.entitys.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(UserRegisterDto userRegisterDto);

    List<User> toUserList(List<UserRegisterDto> userRegisterDtoList);
}
