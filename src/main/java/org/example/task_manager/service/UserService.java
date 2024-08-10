package org.example.task_manager.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.task_manager.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    // Register user
    void createUser(User user, PasswordEncoder encoder);
    // Generating code for 2FA
    void generateConfirmationCode(User user);
    boolean verifyConfirmationCode(User user, String confirmationCode);
    boolean isCodeExpired(User user);
    void acceptUser(User user);
    void resetCodeConfirmation(User user);
}
