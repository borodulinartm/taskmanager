package org.example.task_manager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.task_manager.models.User;

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
