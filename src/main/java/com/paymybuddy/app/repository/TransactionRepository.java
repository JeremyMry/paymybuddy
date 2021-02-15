package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t where t.debtor = :debtorId")
    List<Transaction> findAllByCurrentDebtor(@Param("debtorId") Long debtorId);

    @Query("FROM Transaction t where t.creditor = :creditorId")
    List<Transaction> findAllByCurrentCreditor(@Param("creditorId") Long creditorId);
}
