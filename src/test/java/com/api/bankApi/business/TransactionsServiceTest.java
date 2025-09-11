package com.api.bankApi.business;

import com.api.bankApi.business.mapstruct.TransactionMapper;
import com.api.bankApi.infra.entitys.Account;
import com.api.bankApi.infra.entitys.Transaction;
import com.api.bankApi.infra.entitys.User;
import com.api.bankApi.infra.enums.TransactionEnums;
import com.api.bankApi.infra.enums.UserRole;
import com.api.bankApi.infra.repository.AccountRepository;
import com.api.bankApi.infra.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    @Mock
    private AccountRepository accRepo;

    @Mock
    private TransactionRepository repo;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    TransactionsService service;



    @Test
    @DisplayName("Should throw TransactionExceptions(\"Tipo de transação inválido para deposito\");")
    void processDeposit() {
    }
    @Test
    @DisplayName("Should throw TransactionExceptions(O número informado não corresponde a conta alguma!)")
    void processDeposit2() {
    }
    @Test
    @DisplayName("Should throw TransactionExceptions(O valor de deposito deve ser maior que zero!)")
    void processDeposit3() {
    }
    @Test
    @DisplayName("Should make a deposit and create a transaction")
    void processDeposit4() {
        User destination = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account account = new Account(1L, BigDecimal.ZERO, destination);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.DEPOSIT);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(account);


        service.processDeposit(transaction);

        verify(repo, times(1)).save(any(Transaction.class));
        verify(accRepo, times(1)).save(account);

    }

}