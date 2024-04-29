package com.example.magoyaapp.service;

import com.example.magoyaapp.enums.TransactionType;
import com.example.magoyaapp.model.Account;
import com.example.magoyaapp.model.Transaction;
import com.example.magoyaapp.repository.AccountRepository;
import com.example.magoyaapp.repository.TransactionEventRepository;
import com.example.magoyaapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionEventRepository transactionEventRepository;

    @Test
    public void whenCreateTransactionthenReturnTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId("123");
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(200));

        Account account = new Account();
        account.setId("123");
        account.setAccountNumber("ACC123");
        account.setBalance(new BigDecimal(500));

        when(accountRepository.findById("123")).thenReturn(Optional.of(account));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertThat(createdTransaction).isNotNull();
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void whenCreateTransactionByAccountNumberthenReturnTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId("123");
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(200));

        Account account = new Account();
        account.setId("123");
        account.setAccountNumber("ACC123");
        account.setBalance(new BigDecimal(500));

        when(accountRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(account));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransactionByAccountNumber(transaction, "ACC123");

        assertThat(createdTransaction).isNotNull();
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void whenGetTransactionByIdthenReturnTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId("123");
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(new BigDecimal(200));
        transaction.setId("tran_123");

        when(transactionRepository.findById("tran_123")).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.getTransactionById("tran_123");

        assertThat(foundTransaction).isEqualTo(transaction);
    }
}