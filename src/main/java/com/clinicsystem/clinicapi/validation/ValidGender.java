package com.clinicsystem.clinicapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidGenderValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGender {
    String message() default "Gender must be MALE, FEMALE, or OTHER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
