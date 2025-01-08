package com.sergio.bank.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static String SECRET_KEY = "s3r610";
    private static Algorithm ALGORTIHM = Algorithm.HMAC256(SECRET_KEY);

    public String create(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuer("javaSS")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
                .sign(ALGORTIHM)
                ;
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORTIHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUserName(String jwt) {
        return JWT.require(ALGORTIHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
