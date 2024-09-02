package org.example.task_manager.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super("Task error. Text message: " + message);
    }
}
