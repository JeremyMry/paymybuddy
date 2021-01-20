package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import org.junit.Assert;
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
    TransactionService transactionService;

    @Test
    public void getTransactionTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        User user2 = new User( "paul", "doe", "p.b@testmail.com", "eee", 450);
        Transaction transaction = new Transaction(user, user2, "100", 100);

        userRepository.save(user);
        userRepository.save(user2);
        transactionRepository.save(transaction);

        Assert.assertTrue(transactionService.getTransaction(1).isPresent());
    }

    @Test
    public void getTransaction2Test() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        User user2 = new User( "paul", "doe", "p.b@testmail.com", "eee", 450);
        Transaction transaction = new Transaction(user, user2, "100", 100);
        Transaction transaction2 = new Transaction(user, user2, "100", 100);

        userRepository.save(user);
        userRepository.save(user2);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);

        Assert.assertTrue(transactionService.getTransaction(2).isPresent());
    }

    @Test
    public void getTransactionThatDoesntExistTest() {
        Assert.assertFalse(transactionService.getTransaction(1).isPresent());
    }

    @Test
    public void getAllTransactionsMade() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        User user2 = new User( "paul", "doe", "p.b@testmail.com", "eee", 450);
        Transaction transaction = new Transaction(user2, user, "100", 100);
        Transaction transaction2 = new Transaction(user2, user, "100", 100);

        userRepository.save(user);
        userRepository.save(user2);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);

        Assert.assertEquals(transactionService.getAllTransactionsMade(1).size(), 2);
    }

    @Test
    public void getAllTransactionsMadeWhenThereIsNone() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);

        userRepository.save(user);

        Assert.assertEquals(transactionService.getAllTransactionsMade(1).size(), 0);
    }

    @Test
    public void getAllTransactionsReceived() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        User user2 = new User( "paul", "doe", "p.b@testmail.com", "eee", 450);
        Transaction transaction = new Transaction(user2, user, "100", 100);
        Transaction transaction2 = new Transaction(user2, user, "100", 100);

        userRepository.save(user);
        userRepository.save(user2);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);

        Assert.assertEquals(transactionService.getAllTransactionsReceived(2).size(), 2);
    }

    @Test
    public void getAllTransactionsReceivedWhenThereIsNone() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);

        userRepository.save(user);

        Assert.assertEquals(transactionService.getAllTransactionsMade(1).size(), 0);
    }

    @Test
    public void createTransactionTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);
        User user2 = new User( "paul", "doe", "p.b@testmail.com", "eee", 450);
        userRepository.save(user);
        userRepository.save(user2);

        Transaction transaction = transactionService.createTransaction(new Transaction(user2, user, "100", 100));

        Assert.assertEquals(transaction.getId(), transactionService.getTransaction(1).get().getId());
    }
}
