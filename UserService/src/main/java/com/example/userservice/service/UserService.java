package com.example.userservice.service;

import com.example.userservice.modules.User;
import com.example.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User userRegister(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if(optionalUser.isEmpty()){
            return userRepository.save(user);
        }
        return null;

    }
    public Optional<User> findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
}
