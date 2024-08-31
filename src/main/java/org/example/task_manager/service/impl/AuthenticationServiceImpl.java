package org.example.task_manager.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.exceptions.UserExistsException;
import org.example.task_manager.models.*;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.TokenService;
import org.example.task_manager.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final JWTTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final UserDetailsImpl userDetailsService;
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

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    registrationData.getEmail(),
                    registrationData.getPassword()
            );

            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // When user is register, then it has access to the all
            String accessToken = jwtTokenService.generateAccessToken(myUser);
            String refreshToken = jwtTokenService.generateRefreshToken(myUser);
            tokenService.saveToken(accessToken, myUser);

            return AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
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
        resetAllUserTokens(myUser);

        String accessToken = jwtTokenService.generateAccessToken(myUser);
        String refreshToken = jwtTokenService.generateRefreshToken(myUser);
        tokenService.saveToken(accessToken, myUser);

        // When we authenticate in our system, we need returning token
        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void updateRefreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String bearerToken = authorization.substring(7);
                String userEmail = jwtTokenService.getUserFromToken(bearerToken);
                if (userEmail != null) {
                    Optional<User> optionalUser = userRepository.findByEmail(userEmail);
                    if (optionalUser.isPresent()) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(optionalUser.get().getEmail());
                        if (jwtTokenService.isTokenValid(bearerToken, userDetails)) {
                            resetAllUserTokens(optionalUser.get());

                            String access_token = jwtTokenService.generateAccessToken(optionalUser.get());
                            tokenService.saveToken(access_token, optionalUser.get());

                            AuthenticationResponse authResponse = AuthenticationResponse
                                    .builder()
                                    .accessToken(access_token)
                                    .refreshToken(bearerToken)
                                    .build();

                            new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), authResponse);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            if (exception instanceof ExpiredJwtException) {
                SecurityContextHolder.clearContext();
                handleException(httpServletResponse, "Refresh token has expired, pls log in again", 5);
            } else {
                handleException(httpServletResponse, "Access denied", 10);
            }
        }
    }

    private void handleException(HttpServletResponse response,
                                 String message, Integer code) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse responseBody = ErrorResponse
                .builder()
                .message(message)
                .code(code)
                .timeStamp(LocalDateTime.now().toString())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    private boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private void resetAllUserTokens(User user) {
        List<Token> allTokens = tokenService.getNonExpiredTokensByUser(user);
        allTokens.forEach(token -> {
            // It's revoking user token
            token.setExpired(true);
        });

        tokenService.saveAll(allTokens);
    }
}
