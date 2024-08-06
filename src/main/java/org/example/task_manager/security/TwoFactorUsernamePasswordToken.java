package org.example.task_manager.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

import java.util.List;

public class TwoFactorUsernamePasswordToken extends AbstractAuthenticationToken {
    private final Authentication primary;

    public TwoFactorUsernamePasswordToken(Authentication primary) {
        super(List.of());

        this.primary = primary;
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

