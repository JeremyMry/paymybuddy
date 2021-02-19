package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.LoginRequest;
import com.paymybuddy.app.DTO.SignUpRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthControllerTest {

    @Autowired
    AuthController authController;

    @Test
    public void signUpTest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequest), new ResponseEntity<>(HttpStatus.ACCEPTED));
    }

    @Test
    public void signUpTestWithAlreadyExistingEmail() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("joohnny");
        signUpRequest.setEmail("johndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequest), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void signUpTestWithAlreadyExistingUsername() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setFirstName("john");
        signUpRequest.setLastName("doe");
        signUpRequest.setUsername("johnny");
        signUpRequest.setEmail("joohndoe@testmail.com");
        signUpRequest.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequest), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void signinTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("johnny");
        loginRequest.setPassword("pwd");


        Assertions.assertEquals(authController.authenticateUser(loginRequest).getStatusCode(), HttpStatus.ACCEPTED);
    }

    @Test
    public void signInWithUnregisteredCredentialsTest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("abyc");
        loginRequest.setUsernameOrEmail("joey");

        Exception exception = Assert.assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });

        String expectedMessage = "Bad credentials";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
