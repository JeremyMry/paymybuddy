package com.paymybuddy.app.controller;

import com.paymybuddy.app.config.TokenManager;
import com.paymybuddy.app.model.Login;
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

    @Autowired
    private Logger logger;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

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
    public ResponseEntity<java.lang.String> createToken(@RequestBody Login login) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        } catch (DisabledException e) {
            logger.error("USER_DISABLED");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            logger.error("INVALID_CREDENTIALS");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userService.loadUserByUsername(login.getEmail());
        logger.info("TOKEN GENERATED");
        return new ResponseEntity<>(tokenManager.generateJwtToken(userDetails), HttpStatus.ACCEPTED);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Optional> getProfile(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(userService.getProfile(userId), HttpStatus.OK);
    }

    @PutMapping("/profile/put/firstName/{userId}")
    public ResponseEntity<HttpStatus> updateUserFirstName(@PathVariable("userId") Integer userId, @RequestBody java.lang.String firstName) {
        if(userService.updateUserFirstName(firstName, userId)) {
            logger.info("USER FIRSTNAME UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER FIRSTNAME CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile/put/lastName/{userId}")
    public ResponseEntity<HttpStatus> updateUserLastName(@PathVariable("userId") Integer userId, @RequestBody java.lang.String lastName) {
        if(userService.updateUserLastName(lastName, userId)) {
            logger.info("USER LASTNAME UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER LASTNAME CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile/put/email/{userId}")
    public ResponseEntity<HttpStatus> updateEmail(@PathVariable("userId") Integer userId, @RequestBody java.lang.String email) {
        if(userService.updateEmail(email, userId)) {
            logger.info("USER EMAIL UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER EMAIL CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile/put/password/{userId}")
    public ResponseEntity<HttpStatus> updatePassword(@PathVariable("userId") Integer userId, @RequestBody java.lang.String password) {
        if(userService.updatePassword(password, userId)) {
            logger.info("USER PASSWORD UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER PASSWORD CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile/put/wallet/{userId}")
    public ResponseEntity<HttpStatus> updateWallet(@PathVariable("userId") Integer userId, @RequestBody Integer wallet) {
        if(userService.updateWallet(wallet, userId)) {
            logger.info("USER WALLET UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER WALLET CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
