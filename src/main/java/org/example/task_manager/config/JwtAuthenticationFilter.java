package org.example.task_manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.ErrorResponse;
import org.example.task_manager.models.Token;
import org.example.task_manager.repositry.TokenRepository;
import org.example.task_manager.service.JWTTokenService;
import org.example.task_manager.service.impl.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTTokenService jwtTokenService;
    private final UserDetailsImpl userDetailsService;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String myParam = request.getHeader("Authorization");
            if (!request.getRequestURI().contains("/api/v1/auth")) {
                if (myParam.startsWith("Bearer ")) {
                    final String strToken = myParam.substring(7);
                    Optional<Token> token = tokenRepository.findByTokenAndIsExpiredFalse(strToken);

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
        } catch (ExpiredJwtException exception) {
            handleException(response, "The accession code has expired", 4);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
            handleException(response, "Strange error. Hmm...", -1);
        }
    }

    // Generate and send http response with error
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
}
