package org.example.task_manager.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.task_manager.models.User;
import org.example.task_manager.validation.annotation.PasswordMatching;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, User> {
    @Override
    public void initialize(PasswordMatching passwordMatching) {
        ConstraintValidator.super.initialize(passwordMatching);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getPasswordConfirmation());
    }
}
