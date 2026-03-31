package com.clinicsystem.clinicapi.validation;

import com.clinicsystem.clinicapi.repository.UserRepository;
import com.clinicsystem.clinicapi.service.EmailBloomFilter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;
    private final EmailBloomFilter emailBloomFilter;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank())
            return true;
        if (!emailBloomFilter.mightExist(email))
            return true;
        return !userRepository.existsByEmail(email);
    }
}
