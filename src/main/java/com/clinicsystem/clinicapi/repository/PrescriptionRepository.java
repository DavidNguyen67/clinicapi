package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Prescription;
import com.clinicsystem.clinicapi.model.Prescription.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    Optional<Prescription> findByPrescriptionCode(String prescriptionCode);

    List<Prescription> findByMedicalRecordId(UUID medicalRecordId);

    List<Prescription> findByPatientId(UUID patientId);

    List<Prescription> findByDoctorId(UUID doctorId);

    List<Prescription> findByStatus(PrescriptionStatus status);

    List<Prescription> findByPatientIdAndStatus(UUID patientId, PrescriptionStatus status);

    @Query("SELECT p FROM Prescription p WHERE p.patient.id = :patientId " +
            "ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @Query("SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId " +
            "ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByDoctorIdOrderByDateDesc(@Param("doctorId") UUID doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    boolean existsByPrescriptionCode(String prescriptionCode);
}
