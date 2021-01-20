package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Optional> getTransaction(@PathVariable("transactionId") Integer transactionId) {
        return new ResponseEntity<>(transactionService.getTransaction(transactionId), HttpStatus.OK);
    }

    @GetMapping("/transactions/made/{userId}")
    public ResponseEntity<List> getAllTransactionsMade(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(transactionService.getAllTransactionsMade(userId), HttpStatus.OK);
    }

    @GetMapping("/transactions/received/{userId}")
    public ResponseEntity<List> getAllTransactionsReceived(@PathVariable("userId") Integer userId) {
            return new ResponseEntity<>(transactionService.getAllTransactionsReceived(userId), HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }
}
