package com.api.bankApi.business;

import com.api.bankApi.business.exceptions.TransactionExceptions;
import com.api.bankApi.business.mapstruct.TransactionMapper;
import com.api.bankApi.infra.entitys.Account;
import com.api.bankApi.infra.entitys.Transaction;
import com.api.bankApi.infra.entitys.User;
import com.api.bankApi.infra.enums.TransactionEnums;
import com.api.bankApi.infra.enums.UserRole;
import com.api.bankApi.infra.repository.AccountRepository;
import com.api.bankApi.infra.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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
    @DisplayName("Should throw TransactionExceptions(Tipo de transação inválido para deposito);")
    void processDeposit() {
        User destination = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account account = new Account(1L, BigDecimal.ZERO, destination);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(account);

        Exception thrown = Assertions.assertThrows(TransactionExceptions.class, () -> {service.processDeposit(transaction);});

        Assertions.assertEquals("Tipo de transação inválido para deposito", thrown.getMessage());
    }
    @Test
    @DisplayName("Should throw TransactionExceptions(O número informado não corresponde a conta alguma!)")
    void processDeposit2() {
        User destination = new User(1L, "Rhuan", "12345", UserRole.USER);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.DEPOSIT);
        transaction.setAmount(new BigDecimal("0"));
        transaction.setDestinationAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(null);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processDeposit(transaction));

        Assertions.assertEquals("O número informado não corresponde a conta alguma!", thrown.getMessage());

    }
    @Test
    @DisplayName("Should throw TransactionExceptions(O valor de deposito deve ser maior que zero!)")
    void processDeposit3() {
        User destination = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account account = new Account(1L, BigDecimal.ZERO, destination);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.DEPOSIT);
        transaction.setAmount(new BigDecimal("0"));
        transaction.setDestinationAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(account);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processDeposit(transaction));

        Assertions.assertEquals("O valor de deposito deve ser maior que zero!", thrown.getMessage());


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
    @Test
    @DisplayName("Should make a transfer")
    void processTransfer(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("150"), source);

        User destination = new User(2L, "Rhuan2", "12345", UserRole.USER);
        Account destinationAccount = new Account(1L, new BigDecimal("150"), destination);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(2L);
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);
        when(accRepo.findById(2L)).thenReturn(destinationAccount);

        service.processTransfer(transaction);

        verify(repo, times(1)).save(any(Transaction.class));
        verify(accRepo, times(2)).save(any(Account.class));

    }
    @Test
    @DisplayName("Should throw O tipo de transferencia não corresponde ao tipo de operação")
    void processTransfer2(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("150"), source);

        User destination = new User(2L, "Rhuan2", "12345", UserRole.USER);
        Account destinationAccount = new Account(1L, new BigDecimal("150"), destination);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.DEPOSIT);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(2L);
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);
        when(accRepo.findById(2L)).thenReturn(destinationAccount);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processTransfer(transaction));

        Assertions.assertEquals("O tipo de transferencia não corresponde ao tipo de operação", thrown.getMessage());

    }
    @Test
    @DisplayName("Should throw A conta fonte informada não existe em nosso sistema")
    void processTransfer3(){

        User destination = new User(2L, "Rhuan2", "12345", UserRole.USER);
        Account destinationAccount = new Account(1L, new BigDecimal("150"), destination);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(2L);
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(null);
        when(accRepo.findById(2L)).thenReturn(destinationAccount);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processTransfer(transaction));

        Assertions.assertEquals("A conta fonte informada não existe em nosso sistema", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw Você não tem saldo o suficiente para realizar a transferência")
    void processTransfer4(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("0"), source);

        User destination = new User(2L, "Rhuan2", "12345", UserRole.USER);
        Account destinationAccount = new Account(1L, new BigDecimal("150"), destination);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(2L);
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);
        when(accRepo.findById(2L)).thenReturn(destinationAccount);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processTransfer(transaction));

        Assertions.assertEquals("Você não tem saldo o suficiente para realizar a transferência", thrown.getMessage());

    }
    @Test
    @DisplayName("Should throw A conta de destino informada não existe em nosso sistema")
    void processTransfer5(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("150"), source);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDestinationAccountId(2L);
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);
        when(accRepo.findById(2L)).thenReturn(null);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processTransfer(transaction));

        Assertions.assertEquals("A conta de destino informada não existe em nosso sistema", thrown.getMessage());
    }

    @Test
    @DisplayName("Should process a withdraw normally and update BD")
    void processWithdraw(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("500"), source);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.WITHDRAW);
        transaction.setAmount(new BigDecimal("150"));
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);

        service.processWithdraw(transaction);

        verify(repo, times(1)).save(any(Transaction.class));
        verify(accRepo, times(1)).save(any(Account.class));
    }
    @Test
    @DisplayName("Should throw (O tipo de transação não corresponde a operação de saque)")
    void processWithdraw2(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("500"), source);
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.TRANSFER);
        transaction.setAmount(new BigDecimal("150"));
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processWithdraw(transaction));

        Assertions.assertEquals("O tipo de transação não corresponde a operação de saque", thrown.getMessage());
    }
    @Test
    @DisplayName("Should throw(A conta informada não existe!)")
    void processWithdraw3(){
        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.WITHDRAW);
        transaction.setAmount(new BigDecimal("150"));
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(null);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processWithdraw(transaction));

        Assertions.assertEquals("A conta informada não existe!", thrown.getMessage());
    }
    @Test
    @DisplayName("Should throw(Sua conta não tem saldo o suficiente)")
    void processWithdraw4(){
        User source = new User(1L, "Rhuan", "12345", UserRole.USER);
        Account sourceAccount = new Account(1L, new BigDecimal("0"), source);

        Transaction transaction = new Transaction();
        transaction.setType(TransactionEnums.WITHDRAW);
        transaction.setAmount(new BigDecimal("150"));
        transaction.setSourceAccountId(1L);

        when(accRepo.findById(1L)).thenReturn(sourceAccount);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,()->service.processWithdraw(transaction));

        Assertions.assertEquals("Sua conta não tem saldo o suficiente", thrown.getMessage());

    }
}