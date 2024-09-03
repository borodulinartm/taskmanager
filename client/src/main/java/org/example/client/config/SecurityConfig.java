package org.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> {
            requests.anyRequest().authenticated();
        });

        http.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/oauth2/authorization/taco-admin-client");
        });
        http.oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
