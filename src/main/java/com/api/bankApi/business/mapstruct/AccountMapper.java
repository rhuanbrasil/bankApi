package com.api.bankApi.business.mapstruct;

import com.api.bankApi.business.dtos.AccountRegisterDto;
import com.api.bankApi.infra.entitys.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    Account toAccount(AccountRegisterDto accountRegisterDto);

    List<Account> toAccountList(List<AccountRegisterDto> accountRegisterDtoList);
}
