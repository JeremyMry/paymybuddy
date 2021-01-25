package com.paymybuddy.app.controller;

import com.paymybuddy.app.config.TokenManager;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
public class UserController {

    private final Logger logger;

    public UserController(Logger logger) {
        this.logger = logger;
    }

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Optional> getProfile(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(userService.getProfile(userId), HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        if(userService.createUser(user)) {
            logger.info("ACCOUNT CREATED");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else
            logger.error("EMAIL ALREADY USED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> createToken(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (DisabledException e) {
            logger.error("USER_DISABLED");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            logger.error("INVALID_CREDENTIALS");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        logger.info("TOKEN GENERATED");
        return new ResponseEntity<>(tokenManager.generateJwtToken(userDetails), HttpStatus.ACCEPTED);
    }

    @PutMapping("/profile/put/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Integer userId, @RequestBody User user) {
        logger.info("USER UPDATED");
        return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.OK);
    }
}
