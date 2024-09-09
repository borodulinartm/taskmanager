package org.example.task_manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationConverter converter;

    private final String[] white_list_roles = {
            "api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain buildFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(white_list_roles).permitAll();
                    request.anyRequest().authenticated();
                })
                .sessionManagement(Customizer.withDefaults())
                .oauth2ResourceServer(customizer -> {
                    customizer.jwt(jwtConfigurer -> {
                        jwtConfigurer.jwtAuthenticationConverter(converter);
                    });
                });
        return http.build();
    }

    @Bean
    public CorsConfiguration configuration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
}
