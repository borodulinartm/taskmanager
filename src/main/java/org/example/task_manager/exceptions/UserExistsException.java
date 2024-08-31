package org.example.task_manager.exceptions;

public class UserExistsException extends BaseException {
    public UserExistsException(String message) {
        super("UserExistsException thrown with text message: " + message, 3);
    }
}
