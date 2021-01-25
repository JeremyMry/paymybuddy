package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Token;
import com.paymybuddy.app.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TokenService {

    @Autowired
    TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public Optional<Token> getToken(Integer tokenId) {
        return tokenRepository.findById(tokenId);
    }

}
