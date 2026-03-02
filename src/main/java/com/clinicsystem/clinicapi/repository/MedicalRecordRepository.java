package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Appointment;
import com.clinicsystem.clinicapi.model.MedicalRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "appointment" })
    Optional<MedicalRecord> findByRecordCode(String recordCode);

    @EntityGraph(attributePaths = { "patient", "doctor", "appointment" })
    Optional<MedicalRecord> findByAppointment(Appointment appointment);

    @EntityGraph(attributePaths = { "patient", "doctor", "appointment" })
    Optional<MedicalRecord> findByAppointmentId(UUID appointmentId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "appointment" })
    List<MedicalRecord> findByPatientId(UUID patientId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "appointment" })
    List<MedicalRecord> findByDoctorId(UUID doctorId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "appointment" })
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
            "ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "appointment" })
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId " +
            "ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findByDoctorIdOrderByDateDesc(@Param("doctorId") UUID doctorId);

    boolean existsByRecordCode(String recordCode);

    boolean existsByAppointmentId(UUID appointmentId);
}
