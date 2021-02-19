package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.DTO.JwtAuthenticationResponse;
import com.paymybuddy.app.DTO.LoginRequest;
import com.paymybuddy.app.DTO.SignUpRequest;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.JwtTokenProvider;
import com.paymybuddy.app.service.IAuthService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    Logger logger;


    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public Boolean registerUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            logger.error("Username is already taken!");
            return false;
        } else if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.error("Email Address already in use!");
            return false;
        } else {
            BigDecimal wallet = new BigDecimal("0.00");
            Users user = new Users(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword(), wallet);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("User registered successfully");
            userRepository.save(user);
            return true;
        }
    }
}
