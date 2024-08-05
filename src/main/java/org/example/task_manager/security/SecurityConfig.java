package org.example.task_manager.security;

import org.example.task_manager.controller.CustomAuthenticationFilter;
import org.example.task_manager.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter customAuthenticationFilter() {
        UsernamePasswordAuthenticationFilter cust = new CustomAuthenticationFilter(authenticationManager());
        // We must specify the success and failure handlers otherwise it will not be working
        cust.setAuthenticationSuccessHandler(successHandler());
        cust.setAuthenticationFailureHandler(failureHandler());

        cust.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        return cust;
    }

    @Bean
    public SecurityFilterChain configFilters(HttpSecurity httpSecurity) throws Exception {
        // Authorization for the user role and
        httpSecurity.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/books", "/tasks").hasRole("USER");
            registry.requestMatchers("/**").permitAll();
        });

        httpSecurity.formLogin(login ->
                login.loginPage("/login").defaultSuccessUrl("/books", true));
        httpSecurity.logout(logout ->
                logout.logoutUrl("/logout").logoutSuccessUrl("/home"));

        httpSecurity.addFilter(customAuthenticationFilter());
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());

        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler result = new SavedRequestAwareAuthenticationSuccessHandler();
        result.setDefaultTargetUrl("/books");
        result.setAlwaysUseDefaultTargetUrl(true);

        return result;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler res = new SimpleUrlAuthenticationFailureHandler();
        res.setDefaultFailureUrl("/login?error");
        //res.setUseForward(true);

        return res;
    }
}
