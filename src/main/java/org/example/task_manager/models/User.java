package org.example.task_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.task_manager.validation.annotation.PasswordMatching;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@PasswordMatching
@Table(name = "_user")
public class User implements UserDetails {
    public static final int EXPIRATION_TIME = 60 * 5; // 5 minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "The mail cannot be empty")
    private String email;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String code; // for 2FA
    private boolean confirmCodeEnabled;
    private LocalDateTime dateCodeConfirmed;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }
}
