package org.example.task_manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final String SPRING_SECURITY_CONFIRM_CODE = "confirmation_code";
    private final String confirmCode = SPRING_SECURITY_CONFIRM_CODE;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported");
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String confirmCode = obtainConfirmCode(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username, password
        );
        setDetails(request, token);
        return authenticationManager.authenticate(token);
    }

    protected String obtainConfirmCode(HttpServletRequest request) {
        return request.getParameter(confirmCode);
    }
}
