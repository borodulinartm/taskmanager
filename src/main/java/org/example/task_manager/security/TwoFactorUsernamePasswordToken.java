package org.example.task_manager.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

public class TwoFactorUsernamePasswordToken extends AbstractAuthenticationToken {
    private final Authentication primary;

    public TwoFactorUsernamePasswordToken(Authentication primary) {
        super(primary.getAuthorities());

        this.primary = primary;
        setAuthenticated(true);
    }

    public TwoFactorUsernamePasswordToken(String username, String password) {
        this(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public Object getCredentials() {
        return primary.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return primary.getPrincipal();
    }

    @Override
    public void eraseCredentials() {
        if (primary instanceof CredentialsContainer) {
            ((CredentialsContainer) this.primary).eraseCredentials();
        }
    }
}

