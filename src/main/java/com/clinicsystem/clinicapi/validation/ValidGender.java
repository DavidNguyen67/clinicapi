package com.clinicsystem.clinicapi.validation;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidGenderValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGender {
    String message() default MessageCode.VALIDATION_GENDER_INVALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
