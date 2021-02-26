package com.paymybuddy.app.security;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService userDetailsService;

    // test the loadUserByUsername method / Must return an UserDetails based on the existing user
    @Test
    public void loadUserByUsernameTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "abc", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Assertions.assertEquals(userDetailsService.loadUserByUsername("paulo"),  UserPrincipal.create(user));
    }

    // test the loadUserByUsername method with unknown user / Must throw an exception
    @Test
    public void loadUserByUsernameNoUserFoundTest() {
        Exception exception = Assert.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("paulo"));

        String expectedMessage = "User not found with username or email : paulo";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // test the loadUserById method / Must return an UserDetails based on the existing user id
    @Test
    public void loadUserByIdTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "abc", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Assertions.assertEquals(userDetailsService.loadUserById(1L), UserPrincipal.create(user));
    }

    // test the loadUserById method with unknown user Id / Must throw an exception
    @Test
    public void loadUserByIdNoUserFoundTest() {
        Exception exception = Assert.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserById(1L));

        String expectedMessage = "User not found with id : 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
