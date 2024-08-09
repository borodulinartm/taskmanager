package org.example.task_manager.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.security.TwoFactorUsernamePasswordToken;
import org.example.task_manager.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

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
            // BCrypt uses salt so the same passwords can give different results
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(hashedPassword);
            user.setPasswordConfirmation(hashedPassword);

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
            Authentication tokenWithConfirmCode = new TwoFactorUsernamePasswordToken(authentication);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(tokenWithConfirmCode);

            // Authenticate the user after registration
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        } catch (UsernameNotFoundException exception) {
            log.debug("User not found");
        }
    }

    @Override
    public void generateConfirmationCode(User user) {
        Random random = new Random();

        user.setCode(String.valueOf(random.nextInt(1000, 9999)));
        user.setConfirmCodeEnabled(false);

        userRepository.save(user);
    }

    @Override
    public boolean verifyConfirmationCode(User user, String confirmationCode) {
        return user.getCode().equals(confirmationCode);
    }

    @Override
    public void acceptUser(User user) {
        user.setConfirmCodeEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void resetEnablementFlag(User user) {
        user.setConfirmCodeEnabled(false);
        userRepository.save(user);
    }
}
