package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Appointment;
import com.clinicsystem.clinicapi.model.Appointment.AppointmentStatus;
import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Patient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "doctor.specialty", "service" })
    Optional<Appointment> findByAppointmentCode(String appointmentCode);

    @EntityGraph(attributePaths = { "patient", "doctor", "service" })
    List<Appointment> findByPatient(Patient patient);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "service" })
    List<Appointment> findByPatientId(UUID patientId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "service" })
    List<Appointment> findByDoctor(Doctor doctor);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "service" })
    List<Appointment> findByDoctorId(UUID doctorId);

    @EntityGraph(attributePaths = { "patient", "doctor", "service" })
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    @EntityGraph(attributePaths = { "patient", "doctor", "service" })
    List<Appointment> findByStatus(AppointmentStatus status);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "service" })
    List<Appointment> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate appointmentDate);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "service" })
    List<Appointment> findByPatientIdAndStatus(UUID patientId, AppointmentStatus status);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "service" })
    List<Appointment> findByDoctorIdAndStatus(UUID doctorId, AppointmentStatus status);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "service" })
    List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(
            UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDate = :date " +
            "AND a.startTime <= :endTime AND a.endTime >= :startTime " +
            "AND a.status NOT IN ('cancelled', 'no_show')")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @EntityGraph(attributePaths = { "patient", "patient.user", "doctor", "doctor.user", "service" })
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
            "ORDER BY a.appointmentDate, a.startTime")
    List<Appointment> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = { "patient", "doctor", "doctor.user", "doctor.specialty", "service" })
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
            "ORDER BY a.appointmentDate DESC, a.startTime DESC")
    List<Appointment> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    boolean existsByAppointmentCode(String appointmentCode);
}
