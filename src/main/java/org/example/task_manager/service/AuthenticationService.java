package org.example.task_manager.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.task_manager.models.AuthenticationRequest;
import org.example.task_manager.models.AuthenticationResponse;
import org.example.task_manager.models.Registration;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse registerUser(Registration registrationData);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void updateRefreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse)
             throws IOException;
}
