package org.example.task_manager.service;

import org.example.task_manager.models.User;

public interface TokenService {
    void saveToken(String token, User user);
}
