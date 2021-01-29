package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Transaction> getTransaction(Integer transactionId) { return transactionRepository.findById(transactionId); }

    public List<Transaction> getAllTransactionsMade(Integer userId) { return transactionRepository.findAllByCurrentDebtor(userId); }

    public List<Transaction> getAllTransactionsReceived(Integer userId) { return transactionRepository.findAllByCurrentCreditor(userId); }

    @Transactional
    public Boolean createTransaction(Transaction transaction) {
        String user = transaction.getCreditor();
        String user2 = transaction.getDebtor();

        if (transaction.getAmount() <= 0) {
            return false;
        } else {
            transactionRepository.save(transaction);
            return true;
        }
    }
}
