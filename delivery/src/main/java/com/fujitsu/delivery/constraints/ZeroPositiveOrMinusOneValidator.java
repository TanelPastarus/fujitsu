package com.fujitsu.delivery.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ZeroPositiveOrMinusOneValidator implements ConstraintValidator<ZeroPositiveOrMinusOne, Double> {
    @Override
    public void initialize(ZeroPositiveOrMinusOne constraintAnnotation) {
    }

    @Override
    // Checks if the value is either 0, positive, or -1.
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value == -1 || value >= 0;
    }
}