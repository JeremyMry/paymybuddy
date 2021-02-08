package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Transaction;
import com.paymybuddy.app.model.TransactionProceed;
import com.paymybuddy.app.security.UserPrincipal;

import java.util.List;
import java.util.Optional;

public interface ITransactionService {

    Optional<Transaction> getTransaction(Long transactionId);
    List<Transaction> getAllTransactionsMade(UserPrincipal currentUser);
    List<Transaction> getAllTransactionsReceived(UserPrincipal currentUser);
    void createTransaction(Transaction transaction);
    Boolean transactionComputation(UserPrincipal currentUser, TransactionProceed transactionProceed);
}
