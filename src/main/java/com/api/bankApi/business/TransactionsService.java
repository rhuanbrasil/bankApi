package com.api.bankApi.business;

import com.api.bankApi.business.dtos.TransactionResponseDto;
import com.api.bankApi.business.exceptions.TransactionExceptions;
import com.api.bankApi.business.mapstruct.TransactionMapper;
import com.api.bankApi.infra.entitys.Account;
import com.api.bankApi.infra.entitys.Transaction;
import com.api.bankApi.infra.enums.TransactionEnums;
import com.api.bankApi.infra.repository.AccountRepository;
import com.api.bankApi.infra.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionsService {
    private final AccountRepository accRepo;
    private final TransactionRepository repo;
    private final TransactionMapper mapper;

    @Transactional
    public TransactionResponseDto processDeposit(Transaction transaction){
        Account destination = accRepo.findById(transaction.getDestinationAccountId();
        transaction.setAccount(destination);
        if(transaction.getType() == TransactionEnums.DEPOSIT && transaction.getAccount() != null
                && transaction.getAccount().getBalance() != null
                && transaction.getAccount().getBalance().compareTo(BigDecimal.ZERO) >= 0 ){
                destination.setBalance(destination.getBalance().add(transaction.getAmount()));
                transaction.setTimestamp(LocalDateTime.now());
                repo.save(transaction);
                accRepo.save(destination);
                return mapper.toTransactionResponseDto(transaction);
        }
        throw new TransactionExceptions("O id de destino não foi informado corretamente.");
    }
    @Transactional
    public TransactionResponseDto processWithdraw(Transaction transaction){
        transaction.setAccount(accRepo.findById(transaction.getSourceAccountId()));
        if(transaction.getType() == TransactionEnums.WITHDRAW && transaction.getAccount() != null
           && transaction.getAccount().getBalance().compareTo(transaction.getAmount()) >= 0){
            var currentBalance = transaction.getAccount().getBalance();
            transaction.getAccount().setBalance(currentBalance.subtract(transaction.getAmount()));
            transaction.setTimestamp(LocalDateTime.now());
            repo.save(transaction);
            accRepo.save(transaction.getAccount());
            return mapper.toTransactionResponseDto(transaction);
        }
        throw new TransactionExceptions("O id informado não corresponde a nenhuma conta" +
                                        " ou você não tem saldo o suficiente");
    }@Transactional
    public TransactionResponseDto processTransfer(Transaction transaction){
        Account source = accRepo.findById(transaction.getSourceAccountId());
        Account destinationAccount = accRepo.findById(transaction.getDestinationAccountId());

        transaction.setAccount(accRepo.findById(transaction.getSourceAccountId()));
        if(transaction.getType() == TransactionEnums.TRANSFER && source != null
            && source.getBalance().compareTo(transaction.getAmount()) >= 0
            && destinationAccount != null){
            source.setBalance(source.getBalance().subtract(transaction.getAmount()));
            destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));
            transaction.setTimestamp(LocalDateTime.now());
            repo.save(transaction);
            accRepo.save(source);
            accRepo.save(destinationAccount);
        }
    }
}
