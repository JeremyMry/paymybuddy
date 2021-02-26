package com.paymybuddy.app.controller;

import com.paymybuddy.app.dto.TransactionProceedDto;
import com.paymybuddy.app.security.CurrentUser;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.TransactionServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    Logger logger;

    // Display all the transactions made by the current user
    @GetMapping("/made")
    public ResponseEntity<List> getAllTransactionsMade(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsMade(currentUser), HttpStatus.OK);
    }

    // Display all the transactions received by the current user
    @GetMapping("/received")
    public ResponseEntity<List> getAllTransactionsReceived(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsReceived(currentUser), HttpStatus.OK);
    }

    // create a new transaction
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createTransaction(@CurrentUser UserPrincipal currentUser, @RequestBody TransactionProceedDto transactionProceedDto) {
        if(transactionService.transactionComputation(currentUser, transactionProceedDto)) {
            logger.info("TRANSACTION CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            logger.error("CANNOT CREATE THE TRANSACTION");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
