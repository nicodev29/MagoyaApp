package com.example.magoyaapp.repository;

import com.example.magoyaapp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>{

    @Query("{ 'accountNumber' : ?0 }")
    Optional <Account> findByAccountNumber(String accountNumber);

}
