package com.paymybuddy.app.service;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void getUser() {}

    public void addUser() {}

    public User putUser(User user) {
        Optional<User> userData = userRepository.findById(user.getId());
        if (userData.isPresent()) {
            User user1 = userData.get();
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
            user1.setWallet(user.getWallet());
            return user1;
        } else return null;
    }

    public void deleteUser () {
    }
}
