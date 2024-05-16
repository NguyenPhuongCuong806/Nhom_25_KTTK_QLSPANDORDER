package com.example.userservice.service;

import com.example.userservice.dto.LoginDTO;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;
    private final UserService userService;

    public String login(LoginDTO request){
        if(request.getEmail() == null || request.getPassword() == null) {
            return "login fail";
        }else{
            var user = userRepository.findUserByEmail(request.getEmail())
                    .orElseThrow();
            if(user != null){
                if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                    var userDetail = userDetailService.loadUserByUsername(user.getEmail());
                    var jwtToken = jwtService.generateToken(userDetail);
                    return jwtToken;
                }else{
                    return "user or password not exactly";
                }

            }
            else{
                return "not found user";
            }
        }
    }
}


