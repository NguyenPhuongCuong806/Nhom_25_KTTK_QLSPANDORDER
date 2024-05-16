package com.example.userservice.services;

import com.example.userservice.authen.UserPrincipal;
import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user){
        return userRepository.saveAndFlush(user);
    }
    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal();
        if(user!=null){
            Set<String> authorities = new HashSet<>();
            userPrincipal.setId(user.getId());
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getPassword());
            userPrincipal.setEmail(user.getEmail());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }
}
