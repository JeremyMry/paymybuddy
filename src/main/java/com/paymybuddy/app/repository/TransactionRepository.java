package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}