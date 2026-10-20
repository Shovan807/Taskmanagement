package com.shovanParia.Taskmanagement.Utill;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET =
            "m9SecretKeyMySecrepKeyMySecretKeyyySecretKey123456";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );


    // =========================
    // GENERATE JWT
    // =========================
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(key)
                .compact();
    }


    // =========================
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String jwt) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }


    // =========================
    // CHECK TOKEN VALID
    // =========================
    public boolean isTokenValid(String jwt) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);

            return true;

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }
}