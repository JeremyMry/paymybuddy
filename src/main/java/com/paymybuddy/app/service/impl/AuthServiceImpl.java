package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.AppException;
import com.paymybuddy.app.entity.Role;
import com.paymybuddy.app.entity.RoleName;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.model.ApiResponse;
import com.paymybuddy.app.model.JwtAuthenticationResponse;
import com.paymybuddy.app.model.LoginRequest;
import com.paymybuddy.app.model.SignUpRequest;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.JwtTokenProvider;
import com.paymybuddy.app.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public ApiResponse registerUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ApiResponse(false, "Username is already taken!");
        } else if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ApiResponse(false, "Email Address already in use!");
        } else {
            Users user = new Users(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                          .orElseThrow(() -> new AppException("User Role not set."));
            user.setRoles(Collections.singleton(userRole));
            userRepository.save(user);
            return new ApiResponse(true, "User registered successfully");
        }
    }
}
