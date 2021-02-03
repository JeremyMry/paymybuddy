package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Transaction;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.model.TransactionProceed;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionServiceImpl transactionService;


    @Test
    public void getAllTransactionsMade() {
        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", 100);
        TransactionProceed transactionProceed2 = new TransactionProceed(2L, "new debt", 20);
        TransactionProceed transactionProceed3 = new TransactionProceed(1L, "new debt", 20);

        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
        Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450");
        userRepository.save(user);
        userRepository.save(user2);

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        UserPrincipal userPrincipal1 = UserPrincipal.create(user2);

        Transaction transaction = new Transaction(transactionProceed.getReference(),  transactionProceed.getAmount(), transactionProceed.getCreditor(), userPrincipal.getId());
        Transaction transaction2 = new Transaction(transactionProceed2.getReference(),  transactionProceed2.getAmount(), transactionProceed2.getCreditor(), userPrincipal.getId());
        Transaction transaction3 = new Transaction(transactionProceed3.getReference(),  transactionProceed3.getAmount(), transactionProceed3.getCreditor(), userPrincipal1.getId());
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        Assertions.assertEquals(transactionService.getAllTransactionsMade(userPrincipal).size(), 2);
    }

    @Test
    public void getAllTransactionsMadeWhenThereIsNone() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
        userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertEquals(transactionService.getAllTransactionsMade(userPrincipal).size(), 0);
    }


     @Test
    public void getAllTransactionsReceived() {
         TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", 100);
         TransactionProceed transactionProceed2 = new TransactionProceed(2L, "new debt", 20);
         TransactionProceed transactionProceed3 = new TransactionProceed(1L, "new debt", 20);

         Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
         Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450");
         userRepository.save(user);
         userRepository.save(user2);

         UserPrincipal userPrincipal = UserPrincipal.create(user);
         UserPrincipal userPrincipal1 = UserPrincipal.create(user2);

         Transaction transaction = new Transaction(transactionProceed.getReference(),  transactionProceed.getAmount(), transactionProceed.getCreditor(), userPrincipal.getId());
         Transaction transaction2 = new Transaction(transactionProceed2.getReference(),  transactionProceed2.getAmount(), transactionProceed2.getCreditor(), userPrincipal.getId());
         Transaction transaction3 = new Transaction(transactionProceed3.getReference(),  transactionProceed3.getAmount(), transactionProceed3.getCreditor(), userPrincipal1.getId());
         transactionRepository.save(transaction);
         transactionRepository.save(transaction2);
         transactionRepository.save(transaction3);

         Assertions.assertEquals(transactionService.getAllTransactionsReceived(userPrincipal).size(), 1);
    }

    @Test
    public void getAllTransactionsReceivedWhenThereIsNone() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
        userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertEquals(transactionService.getAllTransactionsReceived(userPrincipal).size(), 0);
    }


    @Test
    public void createTransactionTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
        Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450");
        userRepository.save(user);
        userRepository.save(user2);

        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", 100);
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertTrue(transactionService.createTransaction(userPrincipal, transactionProceed));
        Assertions.assertTrue(transactionService.getTransaction(1L).isPresent());
    }


    @Test
    public void createTransactionWithAmountInferiorAtZeroTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450");
        Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450");
        userRepository.save(user);
        userRepository.save(user2);

        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", -100);
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertFalse(transactionService.createTransaction(userPrincipal, transactionProceed));
        Assertions.assertFalse(transactionService.getTransaction(1L).isPresent());
    }
}
