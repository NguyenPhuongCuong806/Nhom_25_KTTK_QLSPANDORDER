package com.example.userservice.controller;

import com.example.userservice.dto.LoginDTO;
import com.example.userservice.modules.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<User> userRegister(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginDTO request, HttpServletResponse response
    ){
        String jwtToken = authenticationService.login(request);
        if(jwtToken != null){
            return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(jwtToken);
    }

    @PostMapping("/check-jwt")
    public ResponseEntity<String> checkJWT(HttpServletRequest servletRequest){
        String headerJwt = servletRequest.getHeader("Authorization");
        if(headerJwt == null || !headerJwt.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            String jwt = headerJwt.substring(7);
            if(jwt != null){
                String userEmail = jwtService.extractUsername(jwt);
                if(userEmail != null){
                    return ResponseEntity.status(HttpStatus.OK).body(userEmail);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("not found");
    }
}
