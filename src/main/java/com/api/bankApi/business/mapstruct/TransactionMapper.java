package com.api.bankApi.business.mapstruct;

import com.api.bankApi.business.dtos.TransactionResponseDto;
import com.api.bankApi.infra.entitys.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponseDto toTransactionResponseDto(Transaction transaction);

    List<TransactionResponseDto> toTransactionResponseDtos(List<Transaction> transactions);
}
