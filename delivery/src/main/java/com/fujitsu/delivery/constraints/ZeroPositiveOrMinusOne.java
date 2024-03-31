package com.fujitsu.delivery.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation that is used to validate that a number is either 0, positive, or -1.
@Constraint(validatedBy = ZeroPositiveOrMinusOneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZeroPositiveOrMinusOne {
    String message() default "must be 0, positive, or -1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}