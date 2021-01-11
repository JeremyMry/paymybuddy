package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("FROM Transaction t where t.debtor.id = :userId")
    List<Transaction> findAllByCurrentDebtor(@Param("userId") Integer userId);

    @Query("FROM Transaction t where t.creditor.id = :userId")
    List<Transaction> findAllByCurrentCreditor(@Param("userId") Integer userId);
}