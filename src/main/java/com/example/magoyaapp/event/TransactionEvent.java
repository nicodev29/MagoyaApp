package com.example.magoyaapp.event;

import com.example.magoyaapp.enums.TransactionType;
import com.example.magoyaapp.model.Event;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Document(collection = "transaction_events")
public class TransactionEvent extends Event {

    private BigDecimal amount;
    private TransactionType type;

    public TransactionEvent() {
    }

    public TransactionEvent(String accountId, LocalDateTime timestamp, BigDecimal amount, TransactionType type) {
        super(accountId, timestamp);
        this.amount = amount;
        this.type = type;
    }

}