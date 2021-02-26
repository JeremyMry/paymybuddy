package com.paymybuddy.app.controller;

import com.paymybuddy.app.dto.UserProfileDto;
import com.paymybuddy.app.dto.UserSummaryDto;
import com.paymybuddy.app.security.CurrentUser;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.impl.UserServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    Logger logger;

    // Find an other user with his email
    @GetMapping("/findUser")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestParam("email") String email) {
        return new ResponseEntity<>(userServiceImpl.getUserProfile(email), HttpStatus.OK);
    }

    // Display the current user profile
    @GetMapping("/me")
    public ResponseEntity<UserSummaryDto> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        logger.info("GET REQUEST | SUCCESS");
        return new ResponseEntity<>(userServiceImpl.getCurrentUser(currentUser), HttpStatus.OK);
    }

    // Update the current user email
    @PutMapping("/email")
    public ResponseEntity<HttpStatus> updateEmail(@CurrentUser UserPrincipal currentUser, @RequestBody String email) {
        if(userServiceImpl.updateEmail(currentUser, email)) {
            logger.info("USER EMAIL UPDATED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("USER EMAIL CANNOT BE UPDATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update the current user password
    @PutMapping("/password")
    public ResponseEntity<HttpStatus> updatePassword(@CurrentUser UserPrincipal currentUser, @RequestBody String password) {
        userServiceImpl.updatePassword(currentUser, password);
        logger.info("USER PASSWORD UPDATED");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Add money to the current user wallet
    @PutMapping("/wallet/add")
    public ResponseEntity<HttpStatus> addMoneyToTheWallet(@CurrentUser UserPrincipal currentUser, @RequestBody BigDecimal sum) {
        if(userServiceImpl.addMoneyToTheWallet(currentUser, sum)) {
            logger.info("MONEY ADDED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("MONEY CANNOT BE ADDED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Remove money from the current user wallet
    @PutMapping("/wallet/remove")
    public ResponseEntity<HttpStatus> removeMoneyFromTheWallet(@CurrentUser UserPrincipal currentUser, @RequestBody BigDecimal sum) {
        if(userServiceImpl.removeMoneyFromTheWallet(currentUser, sum)) {
            logger.info("MONEY REMOVED");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("MONEY CANNOT BE REMOVED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
