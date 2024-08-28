package org.example.task_manager.config;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.service.impl.UserDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // Inject our user details service
    private final UserDetailsImpl userDetails;
    private final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain filter(HttpSecurity httpSecurity) throws Exception {
        // If we do not block them, all requests will be with 403 Forbidden
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(requests -> {
            requests.requestMatchers(WHITE_LIST_URL).permitAll()
                    .anyRequest().authenticated();
        });

        httpSecurity.authenticationProvider(getProvider());
        httpSecurity.logout(logout -> logout.logoutUrl("/api/v1/auth/logout"));

        return httpSecurity.build();
    }

    // Other beans
    @Bean
    public UserDetailsService getUserDetailService() {
        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(getUserDetailService());

        return provider;
    }

    @Bean
    public AuthenticationManager getManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
