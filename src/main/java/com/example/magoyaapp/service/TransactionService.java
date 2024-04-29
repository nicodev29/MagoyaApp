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

    /*
     * Esta función se encarga de crear una nueva transacción.
     * Primero, se establece la marca de tiempo actual de la transacción.
     * Luego, se busca la cuenta asociada con el ID de cuenta proporcionado en la transacción.
     * Si se encuentra la cuenta, se establece el número de cuenta en la transacción y se actualiza el saldo de la cuenta.
     * Después, se guarda la transacción en la base de datos.
     * Finalmente, se crea un evento de transacción y se devuelve la transacción creada.
     */
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());

        Account account = accountRepository.findById(transaction.getAccountId()).orElse(null);
        if (account != null) {
            transaction.setAccountNumber(account.getAccountNumber());
            updateAccountBalance(transaction);
        }

        Transaction createdTransaction = transactionRepository.save(transaction);
        createTransactionEvent(createdTransaction);
        return createdTransaction;
    }


    /*
     * Esta función se encarga de actualizar el saldo de la cuenta en función del tipo de transacción (depósito o retiro).
     * Si es un depósito, se suma el monto de la transacción al saldo actual de la cuenta.
     * Si es un retiro, se resta el monto de la transacción del saldo actual de la cuenta.
     * Luego, se guarda la cuenta actualizada en la base de datos.
     */
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


    /*
     * Esta función crea un nuevo evento de transacción y lo guarda en la base de datos.
     * El evento de transacción es una entidad separada que registra los detalles de la transacción,
     * como el ID de la cuenta, la marca de tiempo, el monto y el tipo de transacción.
     * Se utiliza para fines de seguimiento de transacciones y controles.
     */
    public void createTransactionEvent(Transaction transaction) {
        TransactionEvent event = new TransactionEvent(transaction.getAccountId(), transaction.getTimestamp(), transaction.getAmount(), transaction.getType());
        transactionEventRepository.save(event);
    }


    /*
     * Esta función se encarga de crear una transacción utilizando el número de cuenta en lugar del ID de cuenta.
     * Primero, se busca la cuenta asociada con el número de cuenta proporcionado.
     * Si se encuentra la cuenta, se establece el ID de cuenta en la transacción.
     * Luego, se llama a la función createTransaction para crear la transacción.
     * Si no se encuentra la cuenta, se lanza una excepción RuntimeException.
     */
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