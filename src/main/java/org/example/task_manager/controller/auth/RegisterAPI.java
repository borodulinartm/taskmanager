package org.example.task_manager.controller.auth;

import jakarta.ws.rs.Consumes;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.body.Authorization;
import org.example.task_manager.service.KeyCloakService;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/register")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegisterAPI {
    private final KeyCloakService keyCloakService;

    @PostMapping
    public void registerUser(@RequestBody @NonNull Authorization authorization) {
        keyCloakService.createUser(authorization);
    }
}
