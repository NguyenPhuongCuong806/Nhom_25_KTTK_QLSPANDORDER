package com.example.userservice.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final static String SECRET_KEY = "kSeTiOKxaLlfVcOB/qdq7IqUnYm4PT+R3kxQ9+5xuXKALc7dbLZTzvzEBMvoaBXV";

    //mã hóa key dạng byte
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //Mã hóa data từ token
    private Claims extractAllClaims(String token){

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //get data từ token
    public <T> T extractClaim(String token, Function<Claims,T> claimsResoler){
        final Claims claims = extractAllClaims(token);
        return claimsResoler.apply(claims);
    }

    @Retryable(maxAttempts = 2, value = RuntimeException.class,
            backoff = @Backoff(delay = 2000, multiplier = 2))
    //get username từ claim
    public String extractUsername(String token){
        System.out.println("check log +++++");
        return extractClaim(token,Claims::getSubject);
    }

    //Đăng ký một token
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        extractClaims.put("authorities", authorities);

        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    //get ra chuỗi token
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    //kiểm tra token
    //kiểm tra xem token có đúng không

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
        // kiểm tra xem token vào có đúng không và đã hết hạn chưa
    }

    //kiểm tra thời hạn
    private boolean isTokenExpired(String token) {
        return extractExpired(token).before(new Date());
    }

    private Date extractExpired(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
