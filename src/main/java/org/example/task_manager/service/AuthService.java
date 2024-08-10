package org.example.task_manager.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    // force authentication after registration user
    void authenticate(String username, String password, HttpServletRequest request);
}
