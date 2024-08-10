package org.example.task_manager.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ConfirmationCodeNotMatchedException extends AuthenticationException {
    public ConfirmationCodeNotMatchedException(String message) {
        super(message);
    }
}
