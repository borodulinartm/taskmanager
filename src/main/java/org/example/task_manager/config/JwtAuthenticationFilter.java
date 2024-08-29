package org.example.task_manager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.Token;
import org.example.task_manager.repositry.TokenRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.impl.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTTokenService jwtTokenService;
    private final UserDetailsImpl userDetailsService;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String myParam = request.getHeader("Authorization");
        if (!request.getRequestURI().contains("/api/v1/auth")) {
            if (myParam.startsWith("Bearer ")) {
                final String strToken = myParam.substring(7);
                Optional<Token> token = tokenRepository.findByToken(strToken);

                // Check via database
                if (token.isPresent()) {
                    final String email = jwtTokenService.getUserFromToken(token.get().getToken());
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        if (jwtTokenService.isTokenValid(token.get().getToken(), userDetails)) {
                            // Set context holder for the current user
                            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                            newAuth.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext().setAuthentication(newAuth);
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
