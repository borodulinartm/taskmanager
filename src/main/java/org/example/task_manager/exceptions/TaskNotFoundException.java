package org.example.task_manager.exceptions;

public class TaskNotFoundException extends BaseException {
    public TaskNotFoundException(String message) {
        super("Task error. Text message: " + message, 2);
    }
}
