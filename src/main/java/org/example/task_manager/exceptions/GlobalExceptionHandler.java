package org.example.task_manager.exceptions;

import lombok.NonNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            BookNotFoundException.class,
            TaskNotFoundException.class,
            UsernameNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleException(BaseException exception) {
        Map<String, Object> details = new HashMap<>();

        details.put("message", exception.getMessage());
        details.put("timestamp", LocalDateTime.now());
        details.put("exception_code",  exception.getExceptionCode());


        return details;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, Object> details = new HashMap<>();

        details.put("timestamp", LocalDateTime.now());
        details.put("status", status.value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        details.put("errors", errors);

        return new ResponseEntity<>(details, headers, status);
    }
}
