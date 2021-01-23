package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.User;
import com.paymybuddy.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
public class UserController {

    @Autowired
    UserService userService;

    /**@GetMapping("/login")
    public ResponseEntity<HttpStatus> signIn(@RequestBody User user) {
        if (userService.getLoginInfo(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }**/

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Optional> getProfile(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(userService.getProfile(userId), HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/profile/put/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Integer userId, @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.OK);
    }
}
