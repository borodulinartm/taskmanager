package org.example.task_manager.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.task_manager.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    // Register user
    void createUser(User user, PasswordEncoder encoder);
    // force authentication after registration user
    void authenticateUser(String username, String password, HttpServletRequest request);
    // Generating code for 2FA
    void generateConfirmationCode(User user);
    boolean verifyConfirmationCode(User user, String confirmationCode);
    void acceptUser(User user);
}
