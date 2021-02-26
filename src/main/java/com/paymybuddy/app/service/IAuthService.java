package com.paymybuddy.app.service;

import com.paymybuddy.app.dto.JwtAuthenticationResponseDto;
import com.paymybuddy.app.dto.LoginRequestDto;
import com.paymybuddy.app.dto.SignUpRequestDto;

public interface IAuthService {

    JwtAuthenticationResponseDto authenticateUser(LoginRequestDto loginRequestDto);
    Boolean registerUser(SignUpRequestDto signUpRequestDto);
}
