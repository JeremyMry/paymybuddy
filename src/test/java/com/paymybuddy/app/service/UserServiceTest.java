package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.DTO.UserProfile;
import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.BankTransferApiServiceMockImpl;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void getUserTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "abc", BigDecimal.ZERO);

        userRepository.save(user);

        assertEquals(userService.getUser(1L).getEmail(), "pdoe@testmail.com");
    }

    @Test
    public void getUserTestThatDoesntExistTest() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser(2L);
        });

        String expectedMessage = "User not found with id : '2'";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getUsernameAvailabilityFalseTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);

        userRepository.save(user);

        Assertions.assertFalse(userService.getUsernameAvailability("paulo"));
    }

    @Test
    public void getUsernameAvailabilityTrueTest() {
        assertTrue(userService.getUsernameAvailability("johnny"));
    }

    @Test
    public void getEmailAvailabilityFalseTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);

        userRepository.save(user);

        Assertions.assertFalse(userService.getEmailAvailability("pdoe@testmail.com"));
    }

    @Test
    public void getEmailAvailabilityTrueTest() {
        assertTrue(userService.getEmailAvailability("johndoe@testmail.com"));
    }

    @Test
    public void getUserProfileTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        UserProfile userProfile = new UserProfile("paulo", "pdoe@testmail.com");

        userRepository.save(user);

        Assertions.assertNotNull(userService.getUserProfile("pdoe@testmail.com"));
        Assertions.assertEquals(userService.getUserProfile("pdoe@testmail.com").getUsername(), userProfile.getUsername());
        Assertions.assertEquals(userService.getUserProfile("pdoe@testmail.com").getEmail(), userProfile.getEmail());
    }

    @Test
    public void getUserProfileThatDoesntExistTest() {
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserProfile("jdoe@testmail.com");
        });

        String expectedMessage = "User not found with email : 'jdoe@testmail.com";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updatePasswordTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        userService.updatePassword(UserPrincipal.create(user), "abc");

        Assertions.assertNotEquals(userService.getUser(1L).getPassword(), user.getPassword());
    }

    @Test
    public void updatePasswordNoCurrentUserTest() {
        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            Users users = new Users();
            userService.updatePassword(UserPrincipal.create(users), "abc");
        });
    }

    @Test
    public void updateEmailTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        assertTrue(userService.updateEmail(UserPrincipal.create(user), "jdoe@testmail.com"));
        assertEquals(userService.getUser(1L).getEmail(), "jdoe@testmail.com");
    }

    @Test
    public void updateEmailWithNonAvailableEmailTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        Users user2 = new Users("john", "doe", "johnny", "jdoe@testmail.com", "pwd", BigDecimal.ZERO);
        userRepository.save(user);
        userRepository.save(user2);

        assertFalse(userService.updateEmail(UserPrincipal.create(user2), "pdoe@testmail.com"));
    }

    @Test
    public void updateEmailWithoutCurrentUser() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);

        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            userService.updateEmail(UserPrincipal.create(user), "jdoe@testmail.com");
        });
    }

    @Test
    public void getCurrentUserTest() {
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        UserSummary userSummary = userService.getCurrentUser(UserPrincipal.create(user));

        assertEquals(userSummary.getEmail(), user.getEmail());
        assertEquals(userSummary.getUsername(), user.getUsername());
    }

    @Test
    public void addMoneyToTheWallet() {
        BigDecimal sum = new BigDecimal("150.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean bool = userService.addMoneyToTheWallet(UserPrincipal.create(user), sum);


        assertEquals(userService.getUser(1L).getWallet(), sum);
        assertTrue(bool);
    }


    @Test
    public void addMoneyToTheWalletSumInferiorTo0() {
        BigDecimal sum = new BigDecimal("-15.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", BigDecimal.ZERO);
        userRepository.save(user);

        Boolean bool = userService.addMoneyToTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }

    @Test
    public void removeMoneyFromTheWalletTest() {
        BigDecimal wallet = new BigDecimal("150.00");
        BigDecimal sum = new BigDecimal("50.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertEquals(userService.getUser(1L).getWallet(), wallet.subtract(sum));
        assertTrue(bool);
    }

    @Test
    public void removeMoneyFromTheWalletTestWithWalletInferiorTo0() {
        BigDecimal wallet = new BigDecimal("15.00");
        BigDecimal sum = new BigDecimal("50.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }

    @Test
    public void removeMoneyFromTheWalletTestWithSumInferiorTo0() {
        BigDecimal wallet = new BigDecimal("15.00");
        BigDecimal sum = new BigDecimal("-50.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        Boolean bool = userService.removeMoneyFromTheWallet(UserPrincipal.create(user), sum);

        assertFalse(bool);
    }


   @Test
    public void updateCreditorWalletTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        userService.updateCreditorWallet(amount, user.getId());

        assertEquals(userService.getUser(1L).getWallet(), wallet.add(amount));
    }

    @Test
    public void updateCreditorWalletNoCreditorTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        assertThrows(ResourceNotFoundException.class, () -> userService.updateCreditorWallet(amount, 2L));
    }

    @Test
    public void updateDebtorWalletTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        userService.updateDebtorWallet(amount, user.getId());

        assertEquals(userService.getUser(1L).getWallet(), wallet.subtract(amount));
    }

    @Test
    public void updateDebtorWalletNoDebtorTest() {
        BigDecimal amount = new BigDecimal("150.00");
        BigDecimal wallet = new BigDecimal("450.00");
        Users user = new Users("paul", "doe", "paulo", "pdoe@testmail.com", "450", wallet);
        userRepository.save(user);

        assertThrows(ResourceNotFoundException.class, () -> userService.updateDebtorWallet(amount, 2L));
    }
}
