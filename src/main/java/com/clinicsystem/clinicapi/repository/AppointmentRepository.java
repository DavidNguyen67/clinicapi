package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Appointment;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.deletedAt IS NOT NULL AND a.deletedAt < :threshold")
    int hardDeleteSoftDeleted(@Param("threshold") LocalDateTime threshold);

}
