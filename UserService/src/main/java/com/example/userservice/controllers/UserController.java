package com.example.userservice.controllers;

import com.example.userservice.authen.UserPrincipal;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.TokenService;
import com.example.userservice.services.UserService;
import com.example.userservice.until.JwtUntil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUntil jwtUntil;
    @Autowired
    TokenService tokenService;
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.createUser(user);

        Map<String, Object> userReponse = new HashMap<>();
        userReponse.put("status",200);
        userReponse.put("message","User created successfully");
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("dob", user.getDob());
        userReponse.put("user", userInfo);
        return ResponseEntity.status(HttpStatus.OK).body(userReponse);
    }
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession httpSession){
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        if(user == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is not valid");
        }
        Token token = new Token();
        token.setToken(jwtUntil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUntil.generateExpirationDate());
        token.setCreateBy(userPrincipal.getId());
        tokenService.createToken(token);

        httpSession.setAttribute("token", token.getToken());
        UserTokenResponse userTokenResponse = new UserTokenResponse();
        userTokenResponse.setUser(userPrincipal);
        userTokenResponse.setToken(token.getToken());

        return ResponseEntity.ok(userTokenResponse);
    }
    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpSession session) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing or invalid");
        }
        String tokenValue = authorizationHeader.substring(7);
        System.out.println("Token Value: " + tokenValue);
        Token token = tokenService.findByToken(tokenValue);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user is logged in");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication: " + authentication);
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        if (!token.getCreateBy().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token does not belong to the authenticated user");
        }

        tokenService.deleteToken(token);
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "User logged out successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Setter
    @Getter
    public class UserTokenResponse {
        private UserPrincipal user;
        private String token;
    }

}
