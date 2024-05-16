package com.example.apigateway.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.security.Key;
import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {
    private final static String SECRET_KEY = "kSeTiOKxaLlfVcOB/qdq7IqUnYm4PT+R3kxQ9+5xuXKALc7dbLZTzvzEBMvoaBXV";
    public boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() .setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public void validateToken(String token) throws Exception {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            JWSVerifier verifier = new MACVerifier(String.valueOf(getSignKey()));
            if (!signedJWT.verify(verifier)) {
                System.out.println("Signature verification failed");
            }
            if (isTokenExpired(String.valueOf(claims))) {
                System.out.println("Token has expired");
            }
            System.out.println("Token is valid");
        } catch (ParseException | JOSEException e) {
            System.out.println("Error validating token: {}" + e.getMessage());
            throw new AuthenticationException("Error validating token") {};
        }
    }

}
