package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.UserProfile;
import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.junit.Assert;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserController userController;

    @Autowired
    MockMvc mockMvc;

    // test the find user controller / must return an User profile and an HttpStatus.OK
    @Test
    public void findUserTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        UserProfile userProfile = new UserProfile("bobby", "bdoe@testmail.com");

        Assertions.assertEquals(Objects.requireNonNull(userController.getUserProfile("bdoe@testmail.com").getBody()).toString(), userProfile.toString());
        Assertions.assertEquals(userController.getUserProfile("bdoe@testmail.com").getStatusCode(), HttpStatus.OK);
    }

    // test the find user controller with unknown email / must throw an exception
    @Test
    public void findUserWithUnknownEmailTest() {

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> userController.getUserProfile("bdoe@testmail.com"));

        String expectedMessage = "User not found with email : 'bdoe@testmail.com'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // test the current user controller / must return an UserSummary and an HttpStatus.OK
    @Test
    public void getCurrentUserTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);
        UserSummary userSummary = new UserSummary(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getWallet());

        Assertions.assertEquals(Objects.requireNonNull(userController.getCurrentUser(UserPrincipal.create(user)).getBody()).toString(), userSummary.toString());
        Assertions.assertEquals(userController.getCurrentUser(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    // test the current user controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void getCurrentUserWhenThereIsNoneTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the update email controller / must return an HttpStatus.OK and update the email
    @Test
    public void updateEmailTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updateEmail(UserPrincipal.create(user), "bodoe@testmail.com").getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userRepository.existsByEmail("bodoe@testmail.com"));
        Assertions.assertFalse(userRepository.existsByEmail("bdoe@testmail.com"));
    }

    // test the update email controller with incorrect value / must return an HttpStatus.BAD_REQUEST
    @Test
    public void updateEmailBadRequestTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updateEmail(UserPrincipal.create(user), "bdoe@testmail.com").getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // test the update email controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void updateEmailWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/email")
                        .content("bdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the update password controller / must update the pasword and return an HttpStatus.OK
    @Test
    public void updatePasswordTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updatePassword(UserPrincipal.create(user), "bvc"), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertNotEquals(userService.getUser(1L).getPassword(), user.getPassword());
    }

    // test the update password controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void updatePasswordWithoutCurrentUserTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/password")
                        .content("bdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the add money to the wallet controller / must return an HttpStatus.OK and update the wallet
    @Test
    public void addMoneyToTheWalletTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        Assertions.assertEquals(userController.addMoneyToTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(10.01)), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(10.01));
    }

    // test the add money to the wallet controller with incorrect values / must return an HttpStatus.BAD_REQUEST
    @Test
    public void addMoneyToTheWalletBadCredentialsTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(1.01));
        userRepository.save(user);

        Assertions.assertEquals(userController.addMoneyToTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(-1.01)), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(1.01));
    }

    // test the add money to the wallet controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void addMoneyToTheWalletWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/wallet/add")
                        .content(String.valueOf(BigDecimal.TEN))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    // test the remove money to the wallet controller / must return an HttpStatus.OK and update the wallet
    @Test
    public void removeMoneyToTheWalletTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(150));
        userRepository.save(user);

        Assertions.assertEquals(userController.removeMoneyFromTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(10.01)), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(139.99));
    }

    // test the remove money to the wallet controller with incorrect values / must return an HttpStatus.BAD_REQUEST
    @Test
    public void removeMoneyToTheWalletBadCredentialsTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(1.01));
        userRepository.save(user);

        Assertions.assertEquals(userController.removeMoneyFromTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(15.01)), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(1.01));
    }

    // test the remove money to the wallet controller without current user / must return an HttpStatus.UNAUTHORIZED
    @Test
    public void removeMoneyToTheWalletWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/wallet/remove")
                        .content(String.valueOf(BigDecimal.TEN))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }
}
