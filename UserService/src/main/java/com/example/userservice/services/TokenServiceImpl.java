package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.until.JwtUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token createToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
    @Override
    public void deleteTokenByValue(String tokenValue) {
        Token token = tokenRepository.findByToken(tokenValue);
        if (token != null) {
            tokenRepository.delete(token);
        }
    }

    @Override
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}
