package org.example.task_manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setCustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> myUser = userRepository.findByUsername(username);
        return myUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
