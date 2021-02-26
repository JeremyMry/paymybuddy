package com.paymybuddy.app.controller;

import com.paymybuddy.app.dto.TransactionProceedDto;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest
public class TransactionControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionController transactionController;

    @Autowired
    MockMvc mockMvc;

    // test the transaction create controller / must create the transaction and return an HttpStatus.CREATED
    @Test
    public void createTransactionTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        BigDecimal bigDecimal = new BigDecimal(450);
        User user = new User("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", bigDecimal, contactList, transactionMadeList, transactionReceivedList);
        User user2 = new User("john", "doe", "john", "jdoe@testmail.com", "pwd", bigDecimal, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user2);

        TransactionProceedDto transactionProceed = new TransactionProceedDto(2L, "debt", BigDecimal.TEN);

        Assertions.assertEquals(transactionController.createTransaction(UserPrincipal.create(user), transactionProceed), new ResponseEntity<>(HttpStatus.CREATED));
    }

    // test the transaction create controller with incorrect values / must return an HttpStatus.BAD_REQUEST
    @Test
    public void createTransactionBadRequestTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("bob", "doe", "bobby", "bdoe@testmail.com", "pwd",BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user2 = new User("john", "doe", "john", "jdoe@testmail.com", "pwd", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);
        userRepository.save(user2);

        TransactionProceedDto transactionProceed = new TransactionProceedDto(2L, "debt", BigDecimal.ZERO);

        Assertions.assertEquals(transactionController.createTransaction(UserPrincipal.create(user), transactionProceed), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // test the transaction create controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void createTransactionNoCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transaction/create")
                        .content("{\"creditor\": 1L,\"reference\":\"debt\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the get all transactions made controller / must return a LIst of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionMade() {
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

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getBody()).size(), 2);
        Assertions.assertEquals(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions made controller when there is no transactions made / must return an empty List of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionMadeWhenThereIsNone() {
        User user = new User();
        userRepository.save(user);

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getBody()).size(), 0);
        Assertions.assertEquals(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions made controller when there is no current user / must return  an HttpStatus.UNAUTHORIZED
    @Test
    public void getAllTransactionMadeWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transaction/made")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the get all transactions received controller / must return a List of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionDone() {
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

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getBody()).size(), 2);
        Assertions.assertEquals(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions received controller when there is no transactions received / must return an empty List of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionDoneWhenThereIsNone() {
        User user = new User();
        userRepository.save(user);

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getBody()).size(), 0);
        Assertions.assertEquals(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions received controller when there is no current user / must return  an HttpStatus.UNAUTHORIZED
    @Test
    public void getAllTransactionDoneWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transaction/received")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }
}
