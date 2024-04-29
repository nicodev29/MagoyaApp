package com.example.magoyaapp.event;

import com.example.magoyaapp.model.Event;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "accounts_events")
public class AccountCreatedEvent extends Event {

    public AccountCreatedEvent() {
    }

    public AccountCreatedEvent(String accountId, LocalDateTime timestamp) {
        super(accountId, timestamp);
    }

}
