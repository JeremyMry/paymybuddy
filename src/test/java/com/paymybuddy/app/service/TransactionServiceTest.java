package com.paymybuddy.app.service;

import com.paymybuddy.app.DTO.TransactionProceed;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.TransactionServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionServiceImpl transactionService;

    // get all transactions made by the current user / must return a List of transactions
    @Test
    public void getAllTransactionsMade() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal amount2 = new BigDecimal("20.00");
        BigDecimal amount3 = new BigDecimal("20.00");
        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", amount);
        TransactionProceed transactionProceed2 = new TransactionProceed(2L, "new debt", amount2);
        TransactionProceed transactionProceed3 = new TransactionProceed(1L, "new debt", amount3);

        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO);
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

    // get all transactions made by the current user when there is none / must return an empty List
    @Test
    public void getAllTransactionsMadeWhenThereIsNone() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertEquals(transactionService.getAllTransactionsMade(userPrincipal).size(), 0);
    }


    // get all transactions received by the current user / must return a List of transactions
     @Test
    public void getAllTransactionsReceived() {
         BigDecimal amount = new BigDecimal("100.00");
         BigDecimal amount2 = new BigDecimal("20.00");
         BigDecimal amount3 = new BigDecimal("20.00");
         TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", amount);
         TransactionProceed transactionProceed2 = new TransactionProceed(2L, "new debt", amount2);
         TransactionProceed transactionProceed3 = new TransactionProceed(1L, "new debt", amount3);

         Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
         Users user2 = new Users( "john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO);
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

    // get all transactions received by the current user when there is none / must return an empty List
    @Test
    public void getAllTransactionsReceivedWhenThereIsNone() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Assertions.assertEquals(transactionService.getAllTransactionsReceived(userPrincipal).size(), 0);
    }

    // create the transaction into the db
    @Test
    public void createTransactionTest() {
        BigDecimal amount = new BigDecimal("10.00");
        Transaction transaction = new Transaction("eeee", amount, 1L, 2L);

        transactionService.createTransaction(transaction);

        Transaction transaction1 = transactionService.getTransaction(1L);
        Assertions.assertEquals(transaction1.getAmount(), amount);
    }

    // make all the transaction logic // return true
    @Test
    public void transactionComputationTest() {
        BigDecimal wallet = new BigDecimal("150.00");
        BigDecimal amount = new BigDecimal("20.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        Users user2 = new Users("john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);
        userRepository.save(user2);
        TransactionProceed transactionProceed = new TransactionProceed(2L, "reference", amount);

        Boolean bool = transactionService.transactionComputation(UserPrincipal.create(user), transactionProceed);

        Assertions.assertTrue(bool);
    }

    // make all the transaction logic / the debtor wallet is inferior the transaction amount / return false
    @Test
    public void transactionComputationAmountSuperiorToDebtorWalletTest() {
        BigDecimal wallet = new BigDecimal("50.00");
        BigDecimal amount = new BigDecimal("60.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        Users user2 = new Users("john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);
        userRepository.save(user2);
        TransactionProceed transactionProceed = new TransactionProceed(2L, "reference", amount);

        Boolean bool = transactionService.transactionComputation(UserPrincipal.create(user), transactionProceed);

        Assertions.assertFalse(bool);
    }

    // get a specific transaction with her id/ return a transaction
    @Test
    public void getTransactionTest() {
        Transaction transaction = new Transaction("eeee", BigDecimal.TEN, 1L, 2L);
        transactionService.createTransaction(transaction);

        Assertions.assertEquals(transactionService.getTransaction(1L).getReference(), "eeee");
    }

    // get a specific transaction that doesnt exist / throw an exception
    @Test
    public void getTransactionThatDoesntExist() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransaction(1L));

        String expectedMessage = "Transaction not found with id : '1'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
