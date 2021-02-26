package com.paymybuddy.app.service;

import com.paymybuddy.app.dto.UserProfileDto;
import com.paymybuddy.app.dto.UserSummaryDto;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.ContactRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.BankTransferApiServiceMockImpl;
import com.paymybuddy.app.service.impl.ContactServiceImpl;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactServiceImpl contactService;

    @Autowired
    UserServiceImpl userService;

    @MockBean
    BankTransferApiServiceMockImpl bankTransferApiService;

    // get a specific user with his id / return an Users
    @Test
    public void getUserTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "abc", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        assertEquals(userService.getUser(1L).getEmail(), "pdoe@testmail.com");
    }

    // get a specific user with his id, when he doesn't exist / throw an exception
    @Test
    public void getUserTestThatDoesntExistTest() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> userService.getUser(2L));

        String expectedMessage = "User not found with id : '2'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // get the username availability. If the the username exist, the method return false
    @Test
    public void getUsernameAvailabilityFalseTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paul", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Assertions.assertFalse(userService.getUsernameAvailability("paul"));
    }

    // get the username availability. If the the username doesn't exist, the method return true
    @Test
    public void getUsernameAvailabilityTrueTest() {
        assertTrue(userService.getUsernameAvailability("johnny"));
    }

    // get the email availability. If the the email exist, the method return false
    @Test
    public void getEmailAvailabilityFalseTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        Assertions.assertFalse(userService.getEmailAvailability("pdoe@testmail.com"));
    }

    // get the email availability. If the the email doesn't exist, the method return true
    @Test
    public void getEmailAvailabilityTrueTest() {
        assertTrue(userService.getEmailAvailability("johndoe@testmail.com"));
    }

    // get a user profile (username + email) with his email // return an userprofile
    @Test
    public void getUserProfileTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        UserProfileDto userProfile = new UserProfileDto("paulo", "pdoe@testmail.com");
        Assertions.assertNotNull(userService.getUserProfile("pdoe@testmail.com"));
        Assertions.assertEquals(userService.getUserProfile("pdoe@testmail.com").getUsername(), userProfile.getUsername());
        Assertions.assertEquals(userService.getUserProfile("pdoe@testmail.com").getEmail(), userProfile.getEmail());
    }

    // get a user profile (username + email) with his email when there is no user with this email / throw ane exception
    @Test
    public void getUserProfileThatDoesntExistTest() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfile("jdoe@testmail.com"));

        String expectedMessage = "User not found with email : 'jdoe@testmail.com";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // update the password when the user is logged (jwt/@currentuser) / the password is updated
    @Test
    public void updatePasswordTest() {
        User user = new User();
        userRepository.save(user);

        userService.updatePassword(UserPrincipal.create(user), "abc");

        Assertions.assertNotEquals(userService.getUser(1L).getPassword(), user.getPassword());
    }

    // update the password when the user isn't logged (jwt), must throw an exception
    @Test
    public void updatePasswordNoCurrentUserTest() {
        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            User user = new User();
            userService.updatePassword(UserPrincipal.create(user), "abc");
        });
    }

    // update the email when the user is logged (jwt/@currentuser) / the method is true, the email is updated
    @Test
    public void updateEmailTest() {
        User user = new User();
        userRepository.save(user);

        assertTrue(userService.updateEmail(UserPrincipal.create(user), "jdoe@testmail.com"));
        assertEquals(userService.getUser(1L).getEmail(), "jdoe@testmail.com");
    }

    // update the email when the user is logged (jwt/@currentuser) and change the contact email if there is contact with the same email
    // the method is true / the contact and user email are update
    @Test
    public void updateUserAndContactEmailTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user1 = new User();
        userRepository.save(user);
        userRepository.save(user1);

        Contact contact = new Contact("pdoe@testmail.com", "paul", user);
        contactRepository.save(contact);

        assertTrue(userService.updateEmail(UserPrincipal.create(user), "jdoe@testmail.com"));
        assertEquals(userService.getUser(1L).getEmail(), "jdoe@testmail.com");
        assertEquals(contactService.getContact(1L).getEmail(), "jdoe@testmail.com");
    }


    // update the email when the user is logged (jwt/@currentuser) with a new email that's not available / the method is false
    @Test
    public void updateEmailWithNonAvailableEmailTest() {
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        User user1 = new User();
        userRepository.save(user);
        userRepository.save(user1);

        assertFalse(userService.updateEmail(UserPrincipal.create(user1), "pdoe@testmail.com"));
    }

    // update the email when the user isn't logged (jwt/@currentuser) / throw an exception
    @Test
    public void updateEmailWithoutCurrentUser() {
        User user = new User();

        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> userService.updateEmail(UserPrincipal.create(user), "jdoe@testmail.com"));
    }

    // get the current user (logged with a jwt) / create an usersummary (firstName, lastName, username, email, wallet)
    @Test
    public void getCurrentUserTest() {
        User user = new User();
        userRepository.save(user);

        UserSummaryDto userSummary = userService.getCurrentUser(UserPrincipal.create(user));

        assertEquals(userSummary.getEmail(), user.getEmail());
        assertEquals(userSummary.getUsername(), user.getUsername());
    }

    // add money to the wallet from simulate bank transfer api / the method must be true
    @Test
    public void addMoneyToTheWallet() {
        BigDecimal sum = new BigDecimal("150.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);


        when(bankTransferApiService.transferMoneyFromTheBankAccountMock(Mockito.any())).thenReturn(true);
        Boolean bool = userService.addMoneyToTheWallet(UserPrincipal.create(user), sum);

        assertEquals(userService.getUser(1L).getWallet(), sum);
        assertTrue(bool);
    }

    // add money to the wallet from simulate bank transfer api with a negative response from the api / the method must be false
    @Test
    public void addMoneyToTheWalletWithBankTransferApiNegativeAnswerTest() {
        BigDecimal sum = new BigDecimal("150.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyFromTheBankAccountMock(Mockito.any())).thenReturn(false);
        Boolean bool = userService.addMoneyToTheWallet(UserPrincipal.create(user), sum);


        assertEquals(userService.getUser(1L).getWallet(), new BigDecimal("0.00"));
        assertFalse(bool);
    }

    // add money to the wallet from simulate bank transfer api with a negative sum / the method must be false
    @Test
    public void addMoneyToTheWalletSumInferiorTo0() {
        BigDecimal sum = new BigDecimal("-15.00");
        User user = new User();
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyFromTheBankAccountMock(Mockito.any())).thenReturn(true);
        Boolean bool = userService.addMoneyToTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }

    // remove money to the wallet a send it via a simulate bank transfer api / the method must be true
    @Test
    public void removeMoneyFromTheWalletTest() {
        BigDecimal wallet = new BigDecimal("150.00");
        BigDecimal sum = new BigDecimal("50.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyToTheBankAccountMock(Mockito.any())).thenReturn(true);
        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertEquals(userService.getUser(1L).getWallet(), wallet.subtract(sum));
        assertTrue(bool);
    }

    // remove money to the wallet a send it via a simulate bank transfer api with a negative response from the api / the method must be false
    @Test
    public void removeMoneyFromTheWalletWithBankTransferApiNegativeAnswerTest() {
        BigDecimal wallet = new BigDecimal("150.00");
        BigDecimal sum = new BigDecimal("50.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyToTheBankAccountMock(Mockito.any())).thenReturn(false);
        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertEquals(userService.getUser(1L).getWallet(), new BigDecimal("150.00"));
        assertFalse(bool);
    }

    // remove money to the wallet a send it via a simulate bank transfer api with a wallet inferior to zero / the method must be false
    @Test
    public void removeMoneyFromTheWalletTestWithWalletInferiorTo0() {
        BigDecimal sum = new BigDecimal("50.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyToTheBankAccountMock(Mockito.any())).thenReturn(true);
        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }

    // remove money to the wallet a send it via a simulate bank transfer api with a sum inferior to zero / the method must be false
    @Test
    public void removeMoneyFromTheWalletTestWithSumInferiorTo0() {
        BigDecimal sum = new BigDecimal("-50.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        when(bankTransferApiService.transferMoneyToTheBankAccountMock(Mockito.any())).thenReturn(true);
        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }

   // Update the creditor wallet when a transaction is created / add money to the wallet
   @Test
    public void updateCreditorWalletTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        userService.updateCreditorWallet(amount, user);

        assertEquals(userService.getUser(1L).getWallet(), wallet.add(amount));
    }

    // Update the debtor wallet when a transaction is created / remove money from the wallet
    @Test
    public void updateDebtorWalletTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        List<Contact> contactList = new ArrayList<>();
        List<Transaction> transactionMadeList = new ArrayList<>();
        List<Transaction> transactionReceivedList = new ArrayList<>();
        User user = new User("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet, contactList, transactionMadeList, transactionReceivedList);
        userRepository.save(user);

        userService.updateDebtorWallet(amount, user);

        assertEquals(userService.getUser(1L).getWallet(), wallet.subtract(amount));
    }
}
