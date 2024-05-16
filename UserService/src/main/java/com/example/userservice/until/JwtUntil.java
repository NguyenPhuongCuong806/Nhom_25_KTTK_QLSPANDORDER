package com.example.userservice.until;

import com.example.userservice.authen.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtUntil {
    private static Logger logger = LoggerFactory.getLogger(JwtUntil.class);
    private static final String SECRET = "The secret for the final project of the Software Architecture and Design course";
    public String generateToken(UserPrincipal user){
        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

            builder.claim("USER", user);
            builder.expirationTime(generateExpirationDate());
            JWTClaimsSet claimsSet = builder.build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            signedJWT.sign(signer);

            token = signedJWT.serialize();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;

    }
    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + 864000000);
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (ParseException | JOSEException e) {
            logger.error(e.getMessage());
        }
        return claims;
    }

    public UserPrincipal getUserFromToken(String token) {
        UserPrincipal user = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            if (claims != null) {
                logger.info("Claims: " + claims.toString());
                logger.info("isTokenExpired: " + isTokenExpired(claims));
                if (!isTokenExpired(claims)) {
                    JSONObject jsonObject = (JSONObject) claims.getClaim("USER");
                        logger.info("User JSON: " + jsonObject.toJSONString());
                    user = new ObjectMapper().readValue(jsonObject.toJSONString(), UserPrincipal.class);
                } else {
                    logger.info("Token has expired");
                }
            } else {
                logger.info("Claims are null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return user;
    }

    private Date getExpirationDateFromToken(JWTClaimsSet claims) {
        return claims != null ? claims.getExpirationTime() : new Date();
    }

    private boolean isTokenExpired(JWTClaimsSet claims) {
        return !getExpirationDateFromToken(claims).before(new Date());
    }}
