package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Patient;
import com.clinicsystem.clinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByPatientCode(String patientCode);

    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserId(UUID userId);

    @Query("SELECT p FROM Patient p ORDER BY p.totalVisits DESC")
    List<Patient> findTopPatientsByVisits();

    @Query("SELECT p FROM Patient p ORDER BY p.loyaltyPoints DESC")
    List<Patient> findTopPatientsByLoyaltyPoints();

    boolean existsByPatientCode(String patientCode);

    boolean existsByUserId(UUID userId);
}
