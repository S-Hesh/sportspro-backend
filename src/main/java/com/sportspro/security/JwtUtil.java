
package com.sportspro.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }

    public String generateToken(String username) {
        Date now = new Date();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expirationMs))
                .sign(getAlgorithm());
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = JWT.require(getAlgorithm()).build().verify(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token, String username) {
        DecodedJWT jwt = JWT.require(getAlgorithm()).build().verify(token);
        return jwt.getSubject().equals(username)
                && jwt.getExpiresAt().after(new Date());
    }
}
