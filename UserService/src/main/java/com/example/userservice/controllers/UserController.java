package com.example.userservice.controllers;

import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.UserService;
import com.example.userservice.until.JwtUntil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    JwtUntil jwtUntil;
    @Autowired
    UserService userService;
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> createUser(@RequestBody User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User userRegister = userService.createUser(user);
        if(userRegister == null ){
            return  ResponseEntity.status(HttpStatus.FOUND).body("User exists");
        }
        Map<String, Object> userReponse = new HashMap<>();
        userReponse.put("status",201);
        userReponse.put("message","User created successfully");
        userReponse.put("user", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userReponse);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody User request) {
        if(request.getUsername() == null || request.getPassword() == null) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login fail");
        } else {
            UserDetails user = userService.loadUserByUsername(request.getUsername());
                if(user != null){
                    if(new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())){
                        String jwtToken = jwtUntil.generateToken(user);
                        Map<String, Object> userReponse = new HashMap<>();
                        userReponse.put("status",200);
                        userReponse.put("message","Login successfully");
                        userReponse.put("user",user);
                        userReponse.put("token",jwtToken);

                        return ResponseEntity.status(HttpStatus.OK).body(userReponse);
                    }else{
                        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or password invalid");
                    }
                }
                else{
                    return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user");
                }
        }
    }
}
