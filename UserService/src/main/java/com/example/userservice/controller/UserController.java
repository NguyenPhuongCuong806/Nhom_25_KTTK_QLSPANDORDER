package com.example.userservice.controller;

import com.example.userservice.dto.LoginDTO;
import com.example.userservice.modules.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final Bucket bucket;
    private final Bucket checkjwtBucket;

    public UserController(PasswordEncoder passwordEncoder, AuthenticationService authenticationService, JwtService jwtService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;

        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        Bandwidth limit1 = Bandwidth.classic(4, Refill.greedy(4, Duration.ofSeconds(10)));

        this.checkjwtBucket = Bucket.builder()
                .addLimit(limit1)
                .build();

        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }


    @PostMapping("/registers")
    public ResponseEntity<User> userRegister(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.OK).body(user1);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO request, HttpServletResponse response
    ) {
        if (bucket.tryConsume(1)) {
            String jwtToken = authenticationService.login(request);
            if (jwtToken != null) {
                return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thông tin đăng nhập không đúng");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Vui lòng đợi thêm 1 phút để thực hiện");
        }
    }

    @PostMapping("/check-jwt")
    public ResponseEntity<String> checkJWT(HttpServletRequest servletRequest) throws Exception {
        String headerJwt = servletRequest.getHeader("Authorization");
        if (headerJwt == null || !headerJwt.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please login");
        } else {
            String jwt = headerJwt.substring(7);
            if (jwt != null) {
                try {
                    String userEmail = jwtService.extractUsername(jwt);
                    if (userEmail != null) {
                        Optional<User> optionalUser = userRepository.findUserByEmail(userEmail);
                        if(checkjwtBucket.tryConsume(1)){
                            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get().getId().toString());
                        }else {
                            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("10s chỉ thực hiện 4 req");

                        }
                    }
                }catch (Exception exception){
                    throw new Exception("check jwt");
                }

            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Not found");
    }
}
