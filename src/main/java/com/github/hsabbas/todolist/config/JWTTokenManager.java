package com.github.hsabbas.todolist.config;

import com.github.hsabbas.todolist.constants.JWTConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTTokenManager {
    @Value("${JWT_SECRET}")
    private String secretKeyEnvVariable;

    public String generateJWTToken(Authentication authentication){
        Assert.notNull(secretKeyEnvVariable, "JWT Secret key is a required environment variable");
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyEnvVariable.getBytes(StandardCharsets.UTF_8));
        Date current = new Date();
        return Jwts.builder()
                .issuer("Todo App")
                .signWith(secretKey)
                .subject(authentication.getName())
                .claim(JWTConstants.EMAIL_CLAIM, authentication.getName())
                .claim(JWTConstants.AUTHORITIES_CLAIM, authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .issuedAt(current)
                .expiration(new Date(current.getTime() + JWTConstants.EXPIRATION_TIME))
                .compact();
    }
}
