package org.example.task_manager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class LoginPageFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // Check if user is authenticated. Conditions:
        // 1. boolean authenticated
        // 2. auth is an implementation of abstract class AbstractAuthenticationToken
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURL = ((HttpServletRequest) servletRequest).getRequestURI();

        if (authentication != null && authentication.isAuthenticated()
                && requestURL.startsWith("/login") && authentication instanceof AbstractAuthenticationToken) {
            ((HttpServletResponse) servletResponse).sendRedirect("/books");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
