package org.example.task_manager.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager manager) {
        this.authenticationManager = manager;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user, PasswordEncoder passwordEncoder) {
        Optional<User> isUserExists = userRepository.findByUsername(user.getUsername());
        if (isUserExists.isEmpty()) {
            // Hashing passwords
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPasswordConfirmation(passwordEncoder.encode(user.getPasswordConfirmation()));

            userRepository.save(user);
        } else {
            log.debug("User already exists");
        }
    }

    @Override
    public void authenticateUser(String username, String password, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Get the current context
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);

            // Authenticate the user after registration
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        } catch (UsernameNotFoundException exception) {
            log.debug("User not found");
        }
    }
}
