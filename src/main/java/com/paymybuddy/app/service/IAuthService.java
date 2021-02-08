package com.paymybuddy.app.service;

import com.paymybuddy.app.model.JwtAuthenticationResponse;
import com.paymybuddy.app.model.LoginRequest;
import com.paymybuddy.app.model.SignUpRequest;

public interface IAuthService {

    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    Boolean registerUser(SignUpRequest signUpRequest);
}