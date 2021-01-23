package com.paymybuddy.app.service;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getProfile(Integer userId) { return userRepository.findById(userId); }

    @Transactional
    public User createUser(User user) { return userRepository.save(user); }

    public User updateUser(User user, Integer userId) {
        User user1 = userRepository.findById(userId).get();
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setWallet(user.getWallet());
        return userRepository.save(user1);
    }

    public Boolean getLoginInfo(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }
}
