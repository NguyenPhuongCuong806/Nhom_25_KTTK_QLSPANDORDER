package com.example.userservice.service;

import com.example.userservice.modules.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User userRegister(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            return null;
        }
        return userRepository.save(user);
    }
    public Optional<User> findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
}
