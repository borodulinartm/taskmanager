package org.example.task_manager.security;

import org.example.task_manager.service.UserService;
import org.example.task_manager.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain configFilters(HttpSecurity httpSecurity) throws Exception {
        // Authorization for the user role and
        httpSecurity.authorizeHttpRequests(registry -> {
            //registry.requestMatchers("/books", "/tasks").hasRole("USER");
            // Check if the confirmation code has approved
            registry.requestMatchers("/books", "/tasks").access(new PostAuthorizationManager());
            registry.requestMatchers("/2fa").access(new TwoFactorAuthorizationManager());
            registry.requestMatchers("/**").permitAll();
        });

        httpSecurity.addFilterBefore(new LoginPageFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.formLogin(login ->
                login.loginPage("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler("/2fa",
                                getSuccessHandler(), userService)));
        httpSecurity.logout(logout ->
                logout.logoutUrl("/logout").logoutSuccessUrl("/home"));

        // it saves data in session automatically
        httpSecurity.securityContext(securityConext -> securityConext.requireExplicitSave(false));

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
    public AuthenticationSuccessHandler getSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler result = new SavedRequestAwareAuthenticationSuccessHandler();
        result.setDefaultTargetUrl("/books");
        result.setAlwaysUseDefaultTargetUrl(true);

        return result;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler res = new SimpleUrlAuthenticationFailureHandler();
        res.setDefaultFailureUrl("/login?error");

        return res;
    }
}
