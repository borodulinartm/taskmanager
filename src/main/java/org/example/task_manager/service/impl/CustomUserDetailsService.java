package org.example.task_manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService, UserService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> myUser = userRepository.findByUsername(username);
        return myUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
}
