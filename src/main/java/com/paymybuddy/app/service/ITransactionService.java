package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.dto.TransactionProceedDto;
import com.paymybuddy.app.security.UserPrincipal;

import java.util.List;

public interface ITransactionService {

    Transaction getTransaction(Long transactionId);
    List<Transaction> getAllTransactionsMade(UserPrincipal currentUser);
    List<Transaction> getAllTransactionsReceived(UserPrincipal currentUser);
    Boolean transactionComputation(UserPrincipal currentUser, TransactionProceedDto transactionProceedDto);
}
