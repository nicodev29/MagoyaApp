package com.example.magoyaapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {

    @Id
    private String id;
    private String accountId;
    private LocalDateTime timestamp;

    public Event(String accountId, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.timestamp = timestamp;
    }
}
