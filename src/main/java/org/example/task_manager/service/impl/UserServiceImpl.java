package org.example.task_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.exceptions.UserExistsException;
import org.example.task_manager.models.AuthenticationRequest;
import org.example.task_manager.models.AuthenticationResponse;
import org.example.task_manager.models.Registration;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.TokenService;
import org.example.task_manager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final JWTTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse registerUser(Registration registrationData) {
        if (!isUserExists(registrationData.getEmail())) {
            User myUser = User.builder()
                    .email(registrationData.getEmail())
                    .nickname(registrationData.getNickname())
                    .password(encoder.encode(registrationData.getPassword()))
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
        return null;
    }

    private boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
