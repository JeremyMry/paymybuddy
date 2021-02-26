package com.paymybuddy.app.service;

import com.paymybuddy.app.dto.TransactionProceedDto;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
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
import java.util.ArrayList;
import java.util.List;

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
        User user = new User();
        User user2 = new User();
        userRepository.save(user);
        userRepository.save(user2);

        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal amount2 = new BigDecimal("20.00");
        TransactionProceedDto transactionProceed = new TransactionProceedDto(2L, "debt", amount);
        TransactionProceedDto transactionProceed2 = new TransactionProceedDto(2L, "new debt", amount2);
        Transaction transaction = new Transaction(transactionProceed.getReference(),  transactionProceed.getAmount(), user2, user);
        Transaction transaction2 = new Transaction(transactionProceed2.getReference(),  transactionProceed2.getAmount(), user2, user);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);

        user.getTransactionMadeList().add(transaction);
        user.getTransactionMadeList().add(transaction2);

        Assertions.assertEquals(transactionService.getAllTransactionsMade(UserPrincipal.create(user)).size(), 2);
    }

    // get all transactions made by the current user when there is none / must return an empty List
    @Test
    public void getAllTransactionsMadeWhenThereIsNone() {
        User user = new User();
        userRepository.save(user);

        Assertions.assertEquals(transactionService.getAllTransactionsMade(UserPrincipal.create(user)).size(), 0);
    }


    // get all transactions received by the current user / must return a List of transactions
     @Test
    public void getAllTransactionsReceived() {
         User user = new User();
         User user2 = new User();
         userRepository.save(user);
         userRepository.save(user2);

         BigDecimal amount = new BigDecimal("100.00");
         BigDecimal amount2 = new BigDecimal("20.00");
         TransactionProceedDto transactionProceed = new TransactionProceedDto(1L, "debt", amount);
         TransactionProceedDto transactionProceed2 = new TransactionProceedDto(1L, "new debt", amount2);
         Transaction transaction = new Transaction(transactionProceed.getReference(),  transactionProceed.getAmount(), user, user2);
         Transaction transaction2 = new Transaction(transactionProceed2.getReference(),  transactionProceed2.getAmount(), user, user2);
         transactionRepository.save(transaction);
         transactionRepository.save(transaction2);

         user.getTransactionReceivedList().add(transaction);
         user.getTransactionReceivedList().add(transaction2);

         Assertions.assertEquals(transactionService.getAllTransactionsReceived(UserPrincipal.create(user)).size(), 2);
    }

    // get all transactions received by the current user when there is none / must return an empty List
    @Test
    public void getAllTransactionsReceivedWhenThereIsNone() {
        User user = new User();
        userRepository.save(user);

        Assertions.assertEquals(transactionService.getAllTransactionsReceived(UserPrincipal.create(user)).size(), 0);
    }

    // make all the transaction logic // return true
    @Test
    public void transactionComputationTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        BigDecimal wallet = new BigDecimal("150.00");
        BigDecimal amount = new BigDecimal("20.00");
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        User user2 = new User("john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user2);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        TransactionProceedDto transactionProceed = new TransactionProceedDto(2L, "reference", amount);
        Boolean bool = transactionService.transactionComputation(userPrincipal, transactionProceed);
        Transaction transaction1 = transactionService.getTransaction(1L);

        Assertions.assertEquals(transaction1.getAmount(), amount);
        Assertions.assertEquals(userRepository.findById(2L).get().getWallet(), amount);
        Assertions.assertEquals(userRepository.findById(1L).get().getWallet(), wallet.subtract(amount.add(BigDecimal.valueOf(0.10))));
        Assertions.assertEquals(transactionService.getAllTransactionsMade(UserPrincipal.create(userRepository.findById(1L).get())).size(), 1);
        Assertions.assertEquals(transactionService.getAllTransactionsReceived(UserPrincipal.create(userRepository.findById(2L).get())).size(), 1);
        Assertions.assertTrue(bool);
    }

    // make all the transaction logic / the debtor wallet is inferior the transaction amount / return false
    @Test
    public void transactionComputationAmountSuperiorToDebtorWalletTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        BigDecimal wallet = new BigDecimal("50.00");
        BigDecimal amount = new BigDecimal("60.00");
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        User user2 = new User("john", "doe", "johnny", "jdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user2);

        TransactionProceedDto transactionProceed = new TransactionProceedDto(2L, "reference", amount);
        Boolean bool = transactionService.transactionComputation(UserPrincipal.create(user), transactionProceed);


        Assertions.assertFalse(bool);
    }

    // get a specific transaction with her id/ return a transaction
    @Test
    public void getTransactionTest() {
        User user = new User();
        User user2 = new User();
        userRepository.save(user);
        userRepository.save(user2);
        Transaction transaction = new Transaction("eeee", BigDecimal.TEN, user, user2);
        transactionRepository.save(transaction);

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
