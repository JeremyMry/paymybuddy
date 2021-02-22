package com.paymybuddy.app.service;

import com.paymybuddy.app.DTO.LoginRequest;
import com.paymybuddy.app.DTO.SignUpRequest;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

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
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        authService.registerUser(signUpRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("johnny");
        loginRequest.setPassword("pwd");

        Assertions.assertFalse(authService.authenticateUser(loginRequest).getAccessToken().isEmpty());
    }

    // log an use that doesn't exist on the db / throw an exception
    @Test
    public void authenticateUserIncorrectCredentialsTest() {
        LoginRequest loginRequest = new LoginRequest();

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(loginRequest));
    }

    // register a new user in the db / return true
    @Test
    public void registerUser() {
        SignUpRequest signUpRequest = new SignUpRequest();
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
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Users user = new Users("paul", "doe", "paulo", "johndoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean aBoolean = authService.registerUser(signUpRequest);

        Assertions.assertFalse(aBoolean);
    }

    // register an user in the db with an username that already exist / return false
    @Test
    public void registerUserWithAnUsernameThatAlreadyExist() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Users user = new Users("paul", "doe", "johnny", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean aBoolean = authService.registerUser(signUpRequest);

        Assertions.assertFalse(aBoolean);
    }
}