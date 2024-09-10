package org.example.task_manager.controller.auth;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.service.KeyCloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
public class UserAPI {
    private final KeyCloakService keyCloakService;

    @PostMapping("/update_password")
    public ResponseEntity<?> updatePassword(@RequestBody @NonNull String newPassword, Principal principal) {
        if (principal instanceof JwtAuthenticationToken) {
            String name = principal.getName();
            if (keyCloakService.resetPassword(name, newPassword)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        throw new RuntimeException("Errors occurred while updating password");
    }
}
