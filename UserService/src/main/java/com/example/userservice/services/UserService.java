package com.example.userservice.services;

import com.example.userservice.authen.UserPrincipal;
import com.example.userservice.models.User;

public interface UserService {
    User createUser(User user);
    UserPrincipal findByUsername(String username);
}
