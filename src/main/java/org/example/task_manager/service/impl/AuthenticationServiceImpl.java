package org.example.task_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.exceptions.UserExistsException;
import org.example.task_manager.models.*;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.TokenService;
import org.example.task_manager.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final JWTTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerUser(Registration registrationData) {
        if (!isUserExists(registrationData.getEmail())) {
            User myUser = User.builder()
                    .email(registrationData.getEmail())
                    .nickname(registrationData.getNickname())
                    .password(encoder.encode(registrationData.getPassword()))
                    .role(Role.USER)
                    .build();
            myUser = userRepository.save(myUser);

            String token = jwtTokenService.generateToken(myUser);
            tokenService.saveToken(token, myUser);

            return AuthenticationResponse
                    .builder()
                    .token(token)
                    .build();
        }
        throw new UserExistsException("User exists in the database");
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User myUser = ((MyUserDetails) auth.getPrincipal()).getUser();
        String token = jwtTokenService.generateToken(myUser);
        tokenService.saveToken(token, myUser);

        // When we authenticate in our system, we need returning token
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }

    private boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
