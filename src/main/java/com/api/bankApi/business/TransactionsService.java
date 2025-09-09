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
        Account destination = accRepo.findById(transaction.getDestinationAccountId());
        transaction.setAccount(destination);
        if(transaction.getType() != TransactionEnums.DEPOSIT){
            throw new TransactionExceptions("Tipo de transação inválido para deposito");
        }
        if(destination == null){
              throw new TransactionExceptions("O número informado não corresponde a conta alguma!");
          }
        if(transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new TransactionExceptions("O valor de deposito deve ser maior que zero!");
        }
                destination.setBalance(destination.getBalance().add(transaction.getAmount()));
                transaction.setTimestamp(LocalDateTime.now());
                repo.save(transaction);
                accRepo.save(destination);
                return mapper.toTransactionResponseDto(transaction);
    }
    @Transactional
    public TransactionResponseDto processWithdraw(Transaction transaction){
        Account sourceAccount = accRepo.findById(transaction.getSourceAccountId());
        transaction.setAccount(sourceAccount);
        if(transaction.getType() != TransactionEnums.WITHDRAW){
            throw new TransactionExceptions("O tipo de transação não corresponde a operação de saque");
        }
        if (sourceAccount == null){
            throw new TransactionExceptions("A conta informada não existe!");
        }
        if(sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0){
            throw new TransactionExceptions("Sua conta não tem saldo o suficiente");
        }

        var currentBalance = transaction.getAccount().getBalance();

        transaction.getAccount().setBalance(currentBalance.subtract(transaction.getAmount()));
        transaction.setTimestamp(LocalDateTime.now());

        repo.save(transaction);
        accRepo.save(transaction.getAccount());

        return mapper.toTransactionResponseDto(transaction);

    }@Transactional
    public TransactionResponseDto processTransfer(Transaction transaction){
        Account sourceAccount = accRepo.findById(transaction.getSourceAccountId());
        Account destinationAccount = accRepo.findById(transaction.getDestinationAccountId());

        transaction.setAccount(sourceAccount);

        if(transaction.getType() != TransactionEnums.TRANSFER){
            throw new TransactionExceptions("O tipo de transferencia não corresponde ao tipo de operação");
        }
        if(sourceAccount == null){
            throw new TransactionExceptions("A conta fonte informada não existe em nosso sistema");
        }
        if(sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0){
            throw new TransactionExceptions("Você não tem saldo o suficiente para realizar a transferência");
        }
        if(destinationAccount == null){
            throw new TransactionExceptions("A conta de destino informada não existe em nosso sistema");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transaction.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));
        transaction.setTimestamp(LocalDateTime.now());

        repo.save(transaction);
        accRepo.save(sourceAccount);
        accRepo.save(destinationAccount);
        return mapper.toTransactionResponseDto(transaction);
    }
}
