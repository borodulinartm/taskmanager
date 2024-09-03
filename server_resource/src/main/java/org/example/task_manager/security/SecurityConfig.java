package org.example.task_manager.security;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain config(HttpSecurity httpSecurity,
                                      AuthenticationManagerBuilder builder) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.OPTIONS).permitAll();
                    request.requestMatchers(HttpMethod.POST, "/api/books")
                            .hasAuthority("SCOPE_writeIngredients");
                    request.requestMatchers(HttpMethod.DELETE, "/api/books")
                            .hasAuthority("SCOPE_deleteIngredients");

                    request.requestMatchers("/**").permitAll();
                });

        httpSecurity.oauth2ResourceServer(oauth2Client -> oauth2Client.jwt(Customizer.withDefaults()));
        httpSecurity.httpBasic(httpSecurityHttpBasicConfigurer -> {
            httpSecurityHttpBasicConfigurer.realmName("task_manager");
        });

        httpSecurity.logout(logout -> logout.logoutSuccessUrl("/"));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.headers(headers -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });

        httpSecurity.authenticationProvider(getProvider());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getUserDetailService() {
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider getProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(getUserDetailService());

        return provider;
    }
}
