package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.exception.BadRequestException;
import com.clinicsystem.clinicapi.model.Patient;
import com.clinicsystem.clinicapi.model.User;
import com.clinicsystem.clinicapi.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(rollbackFor = Exception.class)
    public Patient createPatient(User user) {
        log.info("Creating patient for user: {}", user.getEmail());

        // Check if patient already exists for this user
        if (patientRepository.findByUser(user).isPresent()) {
            throw new BadRequestException(
                    MessageCode.PATIENT_ALREADY_EXISTS,
                    "Patient already exists for this user");
        }

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setPatientCode(generatePatientCode());
        patient.setLoyaltyPoints(0);
        patient.setTotalVisits(0);

        patient = patientRepository.save(patient);
        log.info("Successfully created patient with code: {}", patient.getPatientCode());

        return patient;
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public int getTotalPatients() {
        long count = patientRepository.count();
        return count > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) count;
    }

    private String generatePatientCode() {
        // Format: PT + YYYYMMDD + Random 3 digits
        // Example: PT20260302001
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = LocalDateTime.now().format(formatter);

        String patientCode;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            Random random = new Random();
            int randomNumber = random.nextInt(1000); // 0-999
            patientCode = String.format("PT%s%03d", datePart, randomNumber);
            attempts++;

            if (attempts >= maxAttempts) {
                throw new BadRequestException(
                        MessageCode.SYSTEM_ERROR,
                        "Unable to generate unique patient code");
            }
        } while (patientRepository.existsByPatientCode(patientCode));

        return patientCode;
    }

}
