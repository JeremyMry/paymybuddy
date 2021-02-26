package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.dto.JwtAuthenticationResponseDto;
import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;
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
import java.util.ArrayList;
import java.util.List;

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
    public JwtAuthenticationResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponseDto(jwt);
    }

    @Override
    public Boolean registerUser(SignUpRequestDto signUpRequestDto) {
        if(userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            logger.error("Username is already taken!");
            return false;
        } else if(userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            logger.error("Email Address already in use!");
            return false;
        } else {
            BigDecimal wallet = new BigDecimal("0.00");
            List<Contact> contactList = new ArrayList<>();
            List<Transaction> transactionMadeList = new ArrayList<>();
            List<Transaction> transactionReceivedList = new ArrayList<>();

            User user = new User(signUpRequestDto.getFirstName(), signUpRequestDto.getLastName(), signUpRequestDto.getUsername(), signUpRequestDto.getEmail(), signUpRequestDto.getPassword(), wallet, contactList, transactionMadeList, transactionReceivedList);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("User registered successfully");
            userRepository.save(user);
            return true;
        }
    }
}
