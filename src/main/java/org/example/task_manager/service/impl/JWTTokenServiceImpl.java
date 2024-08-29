package org.example.task_manager.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.User;
import org.example.task_manager.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

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
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Validity by correct user email and expiration date of the token
        String userEmail = getUser(token);
        return userEmail.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    @Override
    public String getUserFromToken(String token) {
        return getUser(token);
    }

    private SecretKey getKey() {
        byte[] keyByres = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyByres);
    }

    private Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaim(String token, Function<Claims, T> functionResolver) {
        Claims claims = getClaims(token);
        return functionResolver.apply(claims);
    }

    private Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private String getUser(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return !getExpirationDate(token).before(new Date());
    }
}
