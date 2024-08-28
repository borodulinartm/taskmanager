package org.example.task_manager.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.User;
import org.example.task_manager.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTTokenServiceImpl implements JWTTokenService {
    @Value("${application.security.jwt.api_key}")
    private String key;

    @Value("${application.security.jwt.expiration_time}")
    private Long expirationTime;

    @Override
    public String generateToken(User user) {
        return Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyByres = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyByres);
    }
}
