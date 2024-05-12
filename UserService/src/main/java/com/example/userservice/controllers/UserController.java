package com.example.userservice.controllers;

import com.example.userservice.authen.UserPrincipal;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.TokenService;
import com.example.userservice.services.UserService;
import com.example.userservice.until.JwtUntil;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUntil jwtUntil;
    @Autowired
    private TokenService tokenService;
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("{\"status\": 200, \"create\": \"ok\"}");
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
        token.setCreateBy(userPrincipal.getUserid());
        tokenService.createToken(token);

        httpSession.setAttribute("token", token.getToken());
        UserTokenResponse userTokenResponse = new UserTokenResponse();
        userTokenResponse.setUser(userPrincipal);
        userTokenResponse.setToken(token.getToken());

        return ResponseEntity.ok(userTokenResponse);
    }
    @Setter
    @Getter
    public class UserTokenResponse {
        private UserPrincipal user;
        private String token;
    }

}
