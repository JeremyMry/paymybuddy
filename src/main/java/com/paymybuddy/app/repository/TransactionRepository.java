package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("FROM Transaction t where c.user.id = :userId")
    List<Transaction> findAllByCurrentUser(@Param("userId") Integer userId);
}