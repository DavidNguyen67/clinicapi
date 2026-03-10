package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Doctor.DoctorStatus;
import com.clinicsystem.clinicapi.model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
        Optional<Doctor> findByDoctorCode(String doctorCode);

        @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.user LEFT JOIN FETCH d.specialty WHERE d.id = :id")
        Optional<Doctor> findByIdWithRelations(@Param("id") UUID id);

        Optional<Doctor> findByUser(User user);

        Optional<Doctor> findByUserId(UUID userId);

        List<Doctor> findByStatus(DoctorStatus status);

        int countByStatus(DoctorStatus status);

        @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.user LEFT JOIN FETCH d.specialty")
        List<Doctor> findActiveForFirstPage(Pageable pageable);

        @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.user LEFT JOIN FETCH d.specialty " +
                        "WHERE d.createdAt < :cur")
        List<Doctor> getAllDoctors(@Param("cur") LocalDateTime cur, Pageable pageable);

}
