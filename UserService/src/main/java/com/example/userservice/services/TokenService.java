package com.example.userservice.services;

import com.example.userservice.models.Token;

public interface TokenService {
    Token createToken(Token token);
    Token findByToken(String token);
    void deleteTokenByValue(String tokenValue);

    void deleteToken(Token token);
}

