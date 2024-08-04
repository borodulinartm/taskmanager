package org.example.task_manager.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.task_manager.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    void createUser(User user, PasswordEncoder encoder);
    void authenticateUser(String username, String password, HttpServletRequest request);
}
