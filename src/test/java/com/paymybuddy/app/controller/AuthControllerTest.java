package com.paymybuddy.app.controller;

import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;
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

    // test the signup controller / must return the HttpStatus.ACCEPTED
    @Test
    public void signUpTest() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequestDto), new ResponseEntity<>(HttpStatus.ACCEPTED));
    }

    // test the signup controller with an already existing email / must return the HttpStatus.BAD_REQUEST
    @Test
    public void signUpTestWithAlreadyExistingEmail() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("joohnny");
        signUpRequestDto.setEmail("johndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequestDto), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // test the signup controller with an already existing username / must return the HttpStatus.BAD_REQUEST
    @Test
    public void signUpTestWithAlreadyExistingUsername() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setFirstName("john");
        signUpRequestDto.setLastName("doe");
        signUpRequestDto.setUsername("johnny");
        signUpRequestDto.setEmail("joohndoe@testmail.com");
        signUpRequestDto.setPassword("pwd");

        Assertions.assertEquals(authController.registerUser(signUpRequestDto), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // test the signin controller / must return the HttpStatus.ACCEPTED
    @Test
    public void signinTest() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsernameOrEmail("johnny");
        loginRequestDto.setPassword("pwd");


        Assertions.assertEquals(authController.authenticateUser(loginRequestDto).getStatusCode(), HttpStatus.ACCEPTED);
    }

    // test the signin controller with unknown credentials / must throw an exception
    @Test
    public void signInWithUnregisteredCredentialsTest() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setPassword("abyc");
        loginRequestDto.setUsernameOrEmail("joey");

        Exception exception = Assert.assertThrows(BadCredentialsException.class, () -> authController.authenticateUser(loginRequestDto));

        String expectedMessage = "Bad credentials";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
