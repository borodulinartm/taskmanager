package org.example.task_manager.controller;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.Registration;
import org.example.task_manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Registration userData) {
        return ResponseEntity.ok(userService.registerUser(userData));
    }
}
