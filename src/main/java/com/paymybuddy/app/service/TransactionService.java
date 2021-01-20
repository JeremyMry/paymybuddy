package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public Optional<Transaction> getTransaction(Integer transactionId) { return transactionRepository.findById(transactionId); }

    public List<Transaction> getAllTransactionsMade(Integer userId) { return transactionRepository.findAllByCurrentDebtor(userId); }

    public List<Transaction> getAllTransactionsReceived(Integer userId) { return transactionRepository.findAllByCurrentCreditor(userId); }

    @Transactional
    public Transaction createTransaction(Transaction transaction) { return transactionRepository.save(transaction); }
}
