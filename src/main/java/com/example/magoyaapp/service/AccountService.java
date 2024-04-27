package com.example.magoyaapp.service;

import com.example.magoyaapp.event.AccountCreatedEvent;
import com.example.magoyaapp.model.Account;
import com.example.magoyaapp.repository.AccountCreatedEventRepository;
import com.example.magoyaapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCreatedEventRepository accountCreatedEventRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountCreatedEventRepository accountCreatedEventRepository) {
        this.accountRepository = accountRepository;
        this.accountCreatedEventRepository = accountCreatedEventRepository;
    }

    public Account createAccount(Account account) {
        Account createdAccount = accountRepository.save(account);

        AccountCreatedEvent event = new AccountCreatedEvent(createdAccount.getId(), LocalDateTime.now());
        accountCreatedEventRepository.save(event);

        return createdAccount;
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElse(null);
    }


}
