package org.example.task_manager.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super("Book not found. Text error: " + message);
    }
}
