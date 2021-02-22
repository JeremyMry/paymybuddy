package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.TransactionProceed;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.Users;
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
        BigDecimal bigDecimal = new BigDecimal(450);
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", bigDecimal);
        userRepository.save(user);
        Users user2 = new Users("john", "doe", "john", "jdoe@testmail.com", "pwd", bigDecimal);
        userRepository.save(user2);
        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", BigDecimal.TEN);

        Assertions.assertEquals(transactionController.createTransaction(UserPrincipal.create(user), transactionProceed), new ResponseEntity<>(HttpStatus.CREATED));
    }

    // test the transaction create controller with incorrect values / must return an HttpStatus.BAD_REQUEST
    @Test
    public void createTransactionBadRequestTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Users user2 = new Users("john", "doe", "john", "jdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user2);
        TransactionProceed transactionProceed = new TransactionProceed(2L, "debt", BigDecimal.ZERO);

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
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Users user1 = new Users("john", "doe", "johnny", "jdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user1);
        Transaction transaction = new Transaction("debt", BigDecimal.TEN, 2L, 1L);
        transactionRepository.save(transaction);
        Transaction transaction1 = new Transaction("debt", BigDecimal.TEN, 2L, 1L);
        transactionRepository.save(transaction1);

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getBody()).size(), 2);
        Assertions.assertEquals(transactionController.getAllTransactionsMade(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions made controller when there is no transactions made / must return an empty List of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionMadeWhenThereIsNone() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
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
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        Users user1 = new Users("john", "doe", "johnny", "jdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user1);
        Transaction transaction = new Transaction("debt", BigDecimal.TEN, 1L, 2L);
        transactionRepository.save(transaction);
        Transaction transaction1 = new Transaction("debt", BigDecimal.TEN, 1L, 2L);
        transactionRepository.save(transaction1);

        Assertions.assertEquals(Objects.requireNonNull(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getBody()).size(), 2);
        Assertions.assertEquals(transactionController.getAllTransactionsReceived(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the get all transactions received controller when there is no transactions received / must return an empty List of transaction and an HttpStatus.OK
    @Test
    public void getAllTransactionDoneWhenThereIsNone() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
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