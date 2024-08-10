package org.example.task_manager.security;

import org.example.task_manager.models.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class PostAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        if (auth != null && auth.getPrincipal() instanceof User curUser) {
            if (curUser.isConfirmCodeEnabled()) {
                return new AuthorizationDecision(true);
            } else {
                return new AuthorizationDecision(false);
            }
        }

        return new AuthorizationDecision(false);
    }
}
