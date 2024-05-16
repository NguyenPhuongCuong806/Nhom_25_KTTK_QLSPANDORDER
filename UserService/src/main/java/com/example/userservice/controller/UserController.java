package com.example.userservice.controller;

import com.example.userservice.modules.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> userRegister(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
