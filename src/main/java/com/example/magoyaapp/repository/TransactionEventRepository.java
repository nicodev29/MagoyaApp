package com.example.magoyaapp.repository;

import com.example.magoyaapp.event.TransactionEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionEventRepository extends MongoRepository<TransactionEvent, String>{
}
