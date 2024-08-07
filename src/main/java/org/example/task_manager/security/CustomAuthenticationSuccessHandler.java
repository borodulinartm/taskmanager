package org.example.task_manager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.task_manager.models.User;
import org.example.task_manager.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private String secondURL;

    // If we have approved our code, then we should redirect to the main page
    private final AuthenticationSuccessHandler primary;
    private final AuthenticationSuccessHandler secondary;

    public CustomAuthenticationSuccessHandler(String url, AuthenticationSuccessHandler primary) {
        this.secondURL = url;

        this.primary = primary;
        this.secondary = new SimpleUrlAuthenticationSuccessHandler(secondURL);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.isConfirmCodeEnabled()) {
            // We are still not authenticated
            TwoFactorUsernamePasswordToken token = new TwoFactorUsernamePasswordToken(authentication);
            SecurityContextHolder.getContext().setAuthentication(token);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            secondary.onAuthenticationSuccess(request, response, authentication);
        } else {
            primary.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
