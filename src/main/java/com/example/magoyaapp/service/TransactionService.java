package com.example.magoyaapp.service;

import com.example.magoyaapp.enums.TransactionType;
import com.example.magoyaapp.event.TransactionEvent;
import com.example.magoyaapp.model.Account;
import com.example.magoyaapp.model.Transaction;
import com.example.magoyaapp.repository.AccountRepository;
import com.example.magoyaapp.repository.TransactionEventRepository;
import com.example.magoyaapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionEventRepository transactionEventRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionEventRepository transactionEventRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionEventRepository = transactionEventRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());
        Transaction createdTransaction = transactionRepository.save(transaction);
        updateAccountBalance(createdTransaction);
        createTransactionEvent(createdTransaction);
        return createdTransaction;
    }

    private void updateAccountBalance(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccountId()).orElse(null);
        if (account != null) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                account.setBalance(account.getBalance().add(transaction.getAmount()));
            } else if (transaction.getType() == TransactionType.WITHDRAWAL) {
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
            }
            accountRepository.save(account);
        }
    }

    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void createTransactionEvent(Transaction transaction) {
        TransactionEvent event = new TransactionEvent(transaction.getAccountId(), transaction.getTimestamp(), transaction.getAmount(), transaction.getType());
        transactionEventRepository.save(event);
    }

    public Transaction createTransactionByAccountNumber(Transaction transaction, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con el número: " + accountNumber));
        transaction.setAccountId(account.getId());

        return createTransaction(transaction);
    }

    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


}