package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class TransactionController {

    @Autowired
    TransactionRepository transactionrepository;

    @GetMapping("/transactions/{id}")
    public ResponseEntity<List> getAllTransactions(@PathVariable("id") Integer userId) {
        try {
            List<Transaction> transactionData = transactionrepository.findAllByCurrentUser(userId);
            return new ResponseEntity<>(transactionData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("transactions")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction transaction1 = transactionrepository.save(new Transaction(transaction.getId(), transaction.getDebtor(), transaction.getCreditor(), transaction.getReference(), transaction.getAmount(), transaction.getUser()));
            return new ResponseEntity<>(transaction1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
