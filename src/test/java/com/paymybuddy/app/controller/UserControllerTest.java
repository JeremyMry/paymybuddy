package com.paymybuddy.app.controller;

import com.paymybuddy.app.DTO.UserProfile;
import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.h2.engine.User;
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
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

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

    @Test
    public void findUserTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        UserProfile userProfile = new UserProfile("bobby", "bdoe@testmail.com");

        Assertions.assertEquals(userController.getUserProfile("bdoe@testmail.com").getBody().toString(), userProfile.toString());
        Assertions.assertEquals(userController.getUserProfile("bdoe@testmail.com").getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void findUserWithUnknownEmailTest() {

        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserProfile("bdoe@testmail.com");
        });

        String expectedMessage = "User not found with email : 'bdoe@testmail.com'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getCurrentUserTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);
        UserSummary userSummary = new UserSummary(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getWallet());

        Assertions.assertEquals(userController.getCurrentUser(UserPrincipal.create(user)).getBody().toString(), userSummary.toString());
        Assertions.assertEquals(userController.getCurrentUser(UserPrincipal.create(user)).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getCurrentUserWhenThereIsNoneTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void updateEmailTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updateEmail(UserPrincipal.create(user), "bodoe@testmail.com").getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(userRepository.existsByEmail("bodoe@testmail.com"));
        Assertions.assertFalse(userRepository.existsByEmail("bdoe@testmail.com"));
    }

    @Test
    public void updateEmailBadRequestTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updateEmail(UserPrincipal.create(user), "bdoe@testmail.com").getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateEmailWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/email")
                        .content("bdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void updatePasswordTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", null);
        userRepository.save(user);

        Assertions.assertEquals(userController.updatePassword(UserPrincipal.create(user), "bvc"), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertNotEquals(userService.getUser(1L).getPassword(), user.getPassword());
    }

    @Test
    public void updatePasswordWithoutCurrentUserTest() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/password")
                        .content("bdoe@testmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void addMoneyToTheWalletTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);

        Assertions.assertEquals(userController.addMoneyToTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(10.01)), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(10.01));
    }

    @Test
    public void addMoneyToTheWalletBadCredentialsTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(1.01));
        userRepository.save(user);

        Assertions.assertEquals(userController.addMoneyToTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(-1.01)), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(1.01));
    }

    @Test
    public void addMoneyToTheWalletWithoutCurrentUser() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user/wallet/add")
                        .content(String.valueOf(BigDecimal.TEN))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void removeMoneyToTheWalletTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(150));
        userRepository.save(user);

        Assertions.assertEquals(userController.removeMoneyFromTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(10.01)), new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(139.99));
    }

    @Test
    public void removeMoneyToTheWalletBadCredentialsTest() {
        Users user = new Users("bob", "doe", "bobby", "bdoe@testmail.com", "pwd", BigDecimal.valueOf(1.01));
        userRepository.save(user);

        Assertions.assertEquals(userController.removeMoneyFromTheWallet(UserPrincipal.create(user), BigDecimal.valueOf(15.01)), new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        Assertions.assertEquals(userService.getUser(1L).getWallet(), BigDecimal.valueOf(1.01));
    }

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
