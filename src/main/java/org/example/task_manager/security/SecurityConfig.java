package org.example.task_manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configFilters(HttpSecurity httpSecurity) throws Exception {
        // Authorization for the user role and
        httpSecurity.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/books", "/tasks").hasRole("USER");
            registry.requestMatchers("/**").permitAll();
        });

        httpSecurity.formLogin(login ->
                login.loginPage("/login").defaultSuccessUrl("/books"));
        httpSecurity.logout(logout ->
                logout.logoutUrl("/logout").logoutSuccessUrl("/"));

        return httpSecurity.build();
    }
}
