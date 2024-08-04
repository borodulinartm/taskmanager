package org.example.task_manager.service;

import org.example.task_manager.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    void createUser(User user, PasswordEncoder encoder);
}
