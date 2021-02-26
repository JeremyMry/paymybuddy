package com.paymybuddy.app.controller;

import com.paymybuddy.app.dto.JwtAuthenticationResponseDto;
import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;
import com.paymybuddy.app.service.impl.AuthServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    Logger logger;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        logger.info("YOU ARE LOGGED");
        return new ResponseEntity<>(authService.authenticateUser(loginRequestDto), HttpStatus.ACCEPTED);
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        if(authService.registerUser(signUpRequestDto)) {
            logger.info("ACCOUNT CREATED");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            logger.error("ACCOUNT CANNOT BE CREATED");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
