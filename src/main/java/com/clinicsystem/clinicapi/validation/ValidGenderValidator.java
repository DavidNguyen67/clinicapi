package com.clinicsystem.clinicapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

import com.clinicsystem.clinicapi.model.User;

public class ValidGenderValidator implements ConstraintValidator<ValidGender, String> {

    private static final List<String> VALID_GENDERS = Arrays.asList(User.Gender.FEMALE.toString(),
            User.Gender.MALE.toString(), User.Gender.OTHER.toString());

    @Override
    public void initialize(ValidGender constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (gender == null || gender.isBlank()) {
            return true; // Let @NotBlank handle this if gender is required
        }

        return VALID_GENDERS.contains(gender.toUpperCase());
    }
}
