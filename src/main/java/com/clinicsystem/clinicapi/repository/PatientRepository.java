package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Patient;
import com.clinicsystem.clinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByUser(User user);

    boolean existsByPatientCode(String patientCode);

}
