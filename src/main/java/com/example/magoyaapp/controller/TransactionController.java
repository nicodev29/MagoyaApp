package com.example.magoyaapp.controller;

import com.example.magoyaapp.model.Event;
import com.example.magoyaapp.model.Transaction;
import com.example.magoyaapp.repository.TransactionRepository;
import com.example.magoyaapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/by-number")
    public ResponseEntity<Transaction> createTransactionByAccountNumber(@RequestBody Transaction transaction, @RequestParam String accountNumber) {
        try {
            Transaction createdTransaction = transactionService.createTransactionByAccountNumber(transaction, accountNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Transaction>> getAllTransactions() {
        Iterable<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }


}
