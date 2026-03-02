package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.DoctorLeave;
import com.clinicsystem.clinicapi.model.DoctorLeave.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, UUID> {

    List<DoctorLeave> findByDoctor(Doctor doctor);

    List<DoctorLeave> findByDoctorId(UUID doctorId);

    List<DoctorLeave> findByDoctorIdAndStatus(UUID doctorId, LeaveStatus status);

    List<DoctorLeave> findByDoctorIdAndLeaveDate(UUID doctorId, LocalDate leaveDate);

    List<DoctorLeave> findByLeaveDateAndStatus(LocalDate leaveDate, LeaveStatus status);

    @Query("SELECT dl FROM DoctorLeave dl WHERE dl.doctor.id = :doctorId " +
            "AND dl.leaveDate = :date AND dl.status = 'approved'")
    List<DoctorLeave> findApprovedLeavesByDoctorAndDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date);

    @Query("SELECT dl FROM DoctorLeave dl WHERE dl.doctor.id = :doctorId " +
            "AND dl.leaveDate BETWEEN :startDate AND :endDate")
    List<DoctorLeave> findByDoctorAndDateRange(
            @Param("doctorId") UUID doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    boolean existsByDoctorIdAndLeaveDateAndStatus(UUID doctorId, LocalDate leaveDate, LeaveStatus status);
}
