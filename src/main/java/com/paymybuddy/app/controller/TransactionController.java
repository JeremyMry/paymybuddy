package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.TransactionProceed;
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
    private TransactionServiceImpl transactionService;

    @Autowired
    private Logger logger;

    @GetMapping("/made")
    public ResponseEntity<List> getAllTransactionsMade(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsMade(currentUser), HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<List> getAllTransactionsReceived(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(transactionService.getAllTransactionsReceived(currentUser), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createTransaction(@CurrentUser UserPrincipal currentUser, @RequestBody TransactionProceed transactionProceed) {
        if(transactionService.transactionComputation(currentUser, transactionProceed)) {
            logger.info("TRANSACTION CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            logger.error("CANNOT CREATE THE TRANSACTION");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
