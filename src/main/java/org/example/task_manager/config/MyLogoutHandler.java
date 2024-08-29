package org.example.task_manager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.Token;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyLogoutHandler implements LogoutHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final JWTTokenService jwtTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String bearer = authorization.substring(7);

            String email = jwtTokenService.getUserFromToken(bearer);
            Optional<User> oUser = userRepository.findByEmail(email);
            if (oUser.isPresent()) {
                User user = oUser.get();

                // Remove all tokens
                List<Token> allTokens = tokenService.getNonExpiredTokensByUser(user);
                allTokens.forEach(token -> {
                    token.setExpired(true);
                });

                tokenService.saveAll(allTokens);
                SecurityContextHolder.clearContext();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }
    }
}
