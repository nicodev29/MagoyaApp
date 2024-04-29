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

    /*
     * Esta función se encarga de crear una nueva cuenta.
     * Primero, se guarda la nueva cuenta en la base de datos utilizando el repositorio AccountRepository.
     * Luego, se crea un objeto AccountCreatedEvent con el ID de la cuenta recién creada y la marca de tiempo actual.
     * Este evento se guarda en la base de datos utilizando el repositorio AccountCreatedEventRepository.
     * Finalmente, se devuelve la cuenta recién creada.
     */
    public Account createAccount(Account account) {
        Account createdAccount = accountRepository.save(account);

        AccountCreatedEvent event = new AccountCreatedEvent(createdAccount.getId(), LocalDateTime.now());
        accountCreatedEventRepository.save(event);

        return createdAccount;
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    /*
     * Esta función busca una cuenta por su número de cuenta y la devuelve.
     * Si no se encuentra una cuenta con el número de cuenta proporcionado, se devuelve null.
     */
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElse(null);

    }
}
