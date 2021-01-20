package com.paymybuddy.app.service;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.h2.tools.DeleteDbFiles;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void getUserTestThatDoesntExist() {
        User user = new User("paul", "doe", "p.b@testmail.com", "eee", 450);

        userRepository.save(user);

        Assert.assertFalse(userService.getProfile(2).isPresent());
    }

    @Test
    public void createUserTest() {
        User user = userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));

        Assert.assertEquals(user.getFirstName(), userService.getProfile(1).get().getFirstName());
    }

    @Test
    public void updateUser() {
        User user = userService.createUser(new User("paul", "doe", "p.b@testmail.com", "eee", 450));
        userRepository.save(user);

        userService.updateUser(new User("John", "doe", "p.b@testmail.com", "eee", 450), 1);

        Assert.assertEquals(userService.getProfile(1).get().getFirstName(), "John");
    }
}
