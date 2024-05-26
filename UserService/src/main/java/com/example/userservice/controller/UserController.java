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
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
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
    private final RateLimiterRegistry rateLimiterRegistry;

    public UserController(PasswordEncoder passwordEncoder, AuthenticationService authenticationService, JwtService jwtService, UserRepository userRepository
    , RateLimiterRegistry rateLimiterRegistry
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }


    @PostMapping("/registers")
    public ResponseEntity<User> userRegister(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.OK).body(user1);
    }

    @PostMapping("/login")
    @RateLimiter(name = "checkJwt",fallbackMethod = "createLoginFallback")
    public ResponseEntity<String> login(
            @RequestBody LoginDTO request, HttpServletResponse response
    ) {
            String jwtToken = authenticationService.login(request);
            if (jwtToken != null) {
                return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thông tin đăng nhập không đúng");
    }

    @RateLimiter(name = "checkJwt",fallbackMethod = "createCommentFallback")
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
                            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get().getId().toString());
                        }

                }catch (Exception exception){
                    throw new Exception("check jwt");
                }

            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("not found");
    }

    public ResponseEntity<?> createCommentFallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Bạn chỉ có thể 4 req trong 10s.");
    }

    public ResponseEntity<?> createLoginFallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Bạn chỉ có thể 10 req trong 1p.");
    }
}
