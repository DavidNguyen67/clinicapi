package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    List<DoctorSchedule> findByDoctor(Doctor doctor);

    List<DoctorSchedule> findByDoctorId(UUID doctorId);

    List<DoctorSchedule> findByDoctorIdAndIsActiveTrue(UUID doctorId);

    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, Integer dayOfWeek);

    List<DoctorSchedule> findByDoctorIdAndDayOfWeekAndIsActiveTrue(UUID doctorId, Integer dayOfWeek);

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek " +
            "AND ds.isActive = true AND ds.startTime <= :time AND ds.endTime >= :time")
    List<DoctorSchedule> findActiveSchedulesByDoctorAndDayAndTime(
            @Param("doctorId") UUID doctorId,
            @Param("dayOfWeek") Integer dayOfWeek,
            @Param("time") LocalTime time);

    boolean existsByDoctorIdAndDayOfWeekAndStartTimeAndEndTime(
            UUID doctorId, Integer dayOfWeek, LocalTime startTime, LocalTime endTime);
}
