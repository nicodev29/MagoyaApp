package com.example.magoyaapp.repository;

import com.example.magoyaapp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>{
}
