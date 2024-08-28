package org.example.task_manager.service;

import org.example.task_manager.models.AuthenticationRequest;
import org.example.task_manager.models.AuthenticationResponse;
import org.example.task_manager.models.Registration;

public interface UserService {
    AuthenticationResponse registerUser(Registration registrationData);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
