package org.example.task_manager.controller;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.AuthenticationRequest;
import org.example.task_manager.models.AuthenticationResponse;
import org.example.task_manager.models.Registration;
import org.example.task_manager.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Registration userData) {
        return ResponseEntity.ok(authenticationService.registerUser(userData));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest userData) {
        return ResponseEntity.ok(authenticationService.authenticate(userData));
    }
}
