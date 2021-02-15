package com.paymybuddy.app.service;

import com.paymybuddy.app.DTO.JwtAuthenticationResponse;
import com.paymybuddy.app.DTO.LoginRequest;
import com.paymybuddy.app.DTO.SignUpRequest;

public interface IAuthService {

    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    Boolean registerUser(SignUpRequest signUpRequest);
}
