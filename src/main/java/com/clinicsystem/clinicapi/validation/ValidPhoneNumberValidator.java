package com.clinicsystem.clinicapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // Vietnamese phone number pattern: starts with 0, followed by 9 digits (total
    // 10 digits)
    // Or starts with +84, followed by 9 digits
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$");

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true; // Let @NotBlank handle this
        }

        // Remove spaces and dashes
        String cleanPhone = phoneNumber.replaceAll("[\\s-]", "");

        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
}
