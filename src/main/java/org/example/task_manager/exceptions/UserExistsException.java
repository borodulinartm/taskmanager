package org.example.task_manager.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super("UserExistsException thrown with text message: " + message);
    }
}
