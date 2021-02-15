package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.DTO.TransactionProceed;
import com.paymybuddy.app.security.UserPrincipal;

import java.util.List;

public interface ITransactionService {

    Transaction getTransaction(Long transactionId);
    List<Transaction> getAllTransactionsMade(UserPrincipal currentUser);
    List<Transaction> getAllTransactionsReceived(UserPrincipal currentUser);
    void createTransaction(Transaction transaction);
    Boolean transactionComputation(UserPrincipal currentUser, TransactionProceed transactionProceed);
}
