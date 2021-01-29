package com.paymybuddy.app.service;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactService contactService;

    public Optional<User> getProfile(Integer userId) { return userRepository.findById(userId); }

    public User findByEmail(java.lang.String email) { return userRepository.findByEmail(email); }

    @Transactional
    public Boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } else return false;
    }

    public Boolean updateUserFirstName(java.lang.String firstName, Integer userId) {
        if(userRepository.findById(userId).isEmpty()) {
            return false;
        } else {
            User user1 = userRepository.findById(userId).get();
            user1.setFirstName(firstName);
            userRepository.save(user1);
            return true;
        }
    }

    public Boolean updateUserLastName(java.lang.String lastName, Integer userId) {
        if(userRepository.findById(userId).isEmpty()) {
            return false;
        } else {
            User user1 = userRepository.findById(userId).get();
            user1.setLastName(lastName);
            userRepository.save(user1);
            return true;
        }
    }

    public Boolean updateWallet(Integer wallet, Integer userId) {
        if(userRepository.findById(userId).isEmpty()) {
            return false;
        } else {
            User user1 = userRepository.findById(userId).get();
            user1.setWallet(wallet);
            userRepository.save(user1);
            return true;
        }
    }

    public Boolean updatePassword(java.lang.String password, Integer userId) {
        if(userRepository.findById(userId).isEmpty()) {
            return false;
        } else {
            User user = userRepository.findById(userId).get();
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
    }

    public Boolean updateEmail(java.lang.String email, Integer userId) {
        if(userRepository.findById(userId).isEmpty()) {
            return false;
        } else {
            User user1 = userRepository.findById(userId).get();
            //also change the contact email link to this userid
            contactService.updateEmail(email, user1.getEmail());
            user1.setEmail(email);
            userRepository.save(user1);
            return true;
        }
    }

    public UserDetails loadUserByUsername(java.lang.String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}
