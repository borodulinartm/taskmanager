package org.example.task_manager.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final int exceptionCode;

    public BaseException(String message, int exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
