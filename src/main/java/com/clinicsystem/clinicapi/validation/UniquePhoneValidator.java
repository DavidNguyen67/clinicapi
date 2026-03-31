package com.clinicsystem.clinicapi.validation;

import com.clinicsystem.clinicapi.repository.UserRepository;
import com.clinicsystem.clinicapi.service.PhoneBloomFilter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final UserRepository userRepository;
    private final PhoneBloomFilter phoneBloomFilter;

    @Override
    public void initialize(UniquePhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank())
            return true;
        if (!phoneBloomFilter.mightExist(email))
            return true;
        return !userRepository.existsByPhone(email);
    }
}
