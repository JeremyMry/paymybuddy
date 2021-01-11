package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable("id") Integer userId) {
        Optional<User> userData = userRepository.findById(userId);
        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/profile")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            User user1 = userRepository.save(new User(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getWallet()));
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/profile/put/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer userId, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) {
            User user1 = userData.get();
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
            user1.setWallet(user.getWallet());
            return new ResponseEntity<>(userRepository.save(user1), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/profile/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Integer userId) {
        try {
            userRepository.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
