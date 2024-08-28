package org.example.task_manager.service;

import org.example.task_manager.models.User;

public interface JWTTokenService {
    String generateToken(User user);
}
