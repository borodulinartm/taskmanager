package org.example.task_manager.service;

import org.example.task_manager.models.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTTokenService {
    String generateToken(User user);
    String getUserFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
