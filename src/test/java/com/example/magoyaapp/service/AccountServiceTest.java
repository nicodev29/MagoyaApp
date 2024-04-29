package com.example.magoyaapp.service;

import com.example.magoyaapp.event.AccountCreatedEvent;
import com.example.magoyaapp.model.Account;
import com.example.magoyaapp.repository.AccountCreatedEventRepository;
import com.example.magoyaapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountCreatedEventRepository accountCreatedEventRepository;

    @Test
    public void testCreateAccount() {
        Account account = new Account();
        account.setAccountNumber("123456");

        given(accountRepository.save(any(Account.class))).willReturn(account);
        given(accountCreatedEventRepository.save(any(AccountCreatedEvent.class)))
                .willReturn(new AccountCreatedEvent(account.getId(), LocalDateTime.now()));

        Account createdAccount = accountService.createAccount(account);

        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getAccountNumber()).isEqualTo(account.getAccountNumber());
    }

    @Test
    public void testGetAccountById() {
        String accountId = "abc123";
        Account account = new Account();
        account.setId(accountId);

        given(accountRepository.findById(accountId)).willReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(accountId);

        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getId()).isEqualTo(accountId);
    }

    @Test
    public void testGetAccountByAccountNumber() {
        String accountNumber = "123456";
        Account account = new Account();
        account.setAccountNumber(accountNumber);

        given(accountRepository.findByAccountNumber(accountNumber)).willReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountByAccountNumber(accountNumber);

        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getAccountNumber()).isEqualTo(accountNumber);
    }
}