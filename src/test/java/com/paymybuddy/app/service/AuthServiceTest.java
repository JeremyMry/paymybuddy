package com.paymybuddy.app.service;

import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserRepository userRepository;

    // log an user that already exist in the db
    @Test
    public void authenticateUserTest() {
        SignUpRequestDto signUpRequest = new SignUpRequestDto();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        authService.registerUser(signUpRequest);

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsernameOrEmail("johnny");
        loginRequest.setPassword("pwd");

        Assertions.assertFalse(authService.authenticateUser(loginRequest).getAccessToken().isEmpty());
    }

    // log an use that doesn't exist on the db / throw an exception
    @Test
    public void authenticateUserIncorrectCredentialsTest() {
        LoginRequestDto loginRequest = new LoginRequestDto();

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(loginRequest));
    }

    // register a new user in the db / return true
    @Test
    public void registerUser() {
        SignUpRequestDto signUpRequest = new SignUpRequestDto();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Boolean aBoolean = authService.registerUser(signUpRequest);

        Assertions.assertTrue(aBoolean);
    }

    // register an user in the db with an email that already exist / return false
    @Test
    public void registerUserWithAnEmailThatAlreadyExist() {
        SignUpRequestDto signUpRequest = new SignUpRequestDto();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "johndoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Boolean aBoolean = authService.registerUser(signUpRequest);

        Assertions.assertFalse(aBoolean);
    }

    // register an user in the db with an username that already exist / return false
    @Test
    public void registerUserWithAnUsernameThatAlreadyExist() {
        SignUpRequestDto signUpRequest = new SignUpRequestDto();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "johnny", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Boolean aBoolean = authService.registerUser(signUpRequest);

        Assertions.assertFalse(aBoolean);
    }
}
