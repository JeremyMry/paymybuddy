package com.paymybuddy.app.service;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    public void getUserTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);

        userRepository.save(user);

        Assert.assertTrue(userService.getProfile(1).isPresent());
    }

    @Test
    public void getUserTestThatDoesntExistTest() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);

        userRepository.save(user);

        Assert.assertFalse(userService.getProfile(2).isPresent());
    }

    @Test
    public void createUserTest() {
        Assert.assertEquals(userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450)), true);
    }

    @Test
    public void createUserTestEmailAlreadyExistTest() {
        userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        Boolean user = userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        Assert.assertEquals(user, false);
    }

    /**@Test
    public void updateUserTest() {
        userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        Boolean user = userService.updateUserNames(new User("John", "doe", "p.b@testmail.com", "eee", 450), 1);

        Assert.assertEquals(userService.getProfile(1).get().getFirstName(), "John");
        Assert.assertEquals(user, true);
    }

    @Test
    public void updateUserIncorrectParamTest() {
        userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        Boolean user = userService.updateUserNames(new User("John", "doe", "p.b@testmail.com", "eee", 450), 2);

        Assert.assertEquals(user, false);
    }**/

    @Test
    public void loadUserByUsernameTest() {
        userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        userService.loadUserByUsername("p.b@testmail.com");

        Assert.assertTrue(userService.loadUserByUsername("p.b@testmail.com").isEnabled());
    }

    @Test
    public void loadUserByUsernameIncorrectParamTest() {
        userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));

        Assert.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("pe.b@testmail.com");
        });
    }
}
