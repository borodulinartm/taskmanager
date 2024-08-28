package org.example.task_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.MyUserDetails;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> myUser = userRepository.findByEmail(username);
        if (myUser.isPresent()) {
            return new MyUserDetails(myUser.get());
        }

        throw new UsernameNotFoundException("User not found");
    }
}
