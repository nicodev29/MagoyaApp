package com.example.magoyaapp.repository;

import com.example.magoyaapp.event.AccountCreatedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCreatedEventRepository extends MongoRepository<AccountCreatedEvent, String>{
}
