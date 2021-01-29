package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.service.TransactionService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private Logger logger;

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Optional> getTransaction(@PathVariable("transactionId") Integer transactionId) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getTransaction(transactionId), HttpStatus.OK);
    }

    @GetMapping("/transactions/made/{userId}")
    public ResponseEntity<List> getAllTransactionsMade(@PathVariable("userId") Integer userId) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsMade(userId), HttpStatus.OK);
    }

    @GetMapping("/transactions/received/{userId}")
    public ResponseEntity<List> getAllTransactionsReceived(@PathVariable("userId") Integer userId) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsReceived(userId), HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<HttpStatus> createTransaction(@RequestBody Transaction transaction) {
        if(transactionService.createTransaction(transaction)) {
            logger.info("TRANSACTION CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            logger.error("CANNOT CREATE THE TRANSACTION");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
