package org.example.task_manager.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.task_manager.validation.impl.PasswordMatchingValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchingValidator.class)
@Documented
public @interface PasswordMatching {
    String message() default "Password does not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
