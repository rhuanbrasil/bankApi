package com.api.bankApi.business;

import com.api.bankApi.business.dtos.TransactionResponseDto;
import com.api.bankApi.business.exceptions.TransactionExceptions;
import com.api.bankApi.business.mapstruct.TransactionMapper;
import com.api.bankApi.infra.entitys.Transaction;
import com.api.bankApi.infra.enums.TransactionEnums;
import com.api.bankApi.infra.repository.AccountRepository;
import com.api.bankApi.infra.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final AccountRepository accRepo;
    private final TransactionRepository repo;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionResponseDto processDeposit(Transaction transaction){
        transaction.setAccount(accRepo.findById(transaction.getDestinationAccountId()));
        if(transaction.getType() == TransactionEnums.DEPOSIT && transaction.getAccount() != null
                && transaction.getAccount().getBalance() != null
                && transaction.getAccount().getBalance().compareTo(BigDecimal.ZERO) >= 0 ){
                transaction.getAccount().setBalance(transaction.getAmount());
                repo.save(transaction);
                return mapper.toTransactionResponseDto(transaction);
        }
        return null;
    }
}
