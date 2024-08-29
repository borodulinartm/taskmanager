package org.example.task_manager.service;

import org.example.task_manager.models.Token;
import org.example.task_manager.models.User;

import java.util.List;

public interface TokenService {
    void saveToken(String token, User user);
    void saveAll(List<Token> tokens);
    List<Token> getNonExpiredTokensByUser(User user);
}
