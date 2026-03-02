package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Doctor.DoctorStatus;
import com.clinicsystem.clinicapi.model.Specialty;
import com.clinicsystem.clinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByDoctorCode(String doctorCode);

    Optional<Doctor> findByUser(User user);

    Optional<Doctor> findByUserId(UUID userId);

    List<Doctor> findBySpecialty(Specialty specialty);

    List<Doctor> findBySpecialtyId(UUID specialtyId);

    List<Doctor> findByStatus(DoctorStatus status);

    List<Doctor> findByStatusAndIsFeaturedTrue(DoctorStatus status);

    List<Doctor> findBySpecialtyIdAndStatus(UUID specialtyId, DoctorStatus status);

    @Query("SELECT d FROM Doctor d WHERE d.status = :status ORDER BY d.averageRating DESC, d.totalReviews DESC")
    List<Doctor> findTopRatedDoctors(@Param("status") DoctorStatus status);

    @Query("SELECT d FROM Doctor d WHERE d.specialty.id = :specialtyId AND d.status = :status ORDER BY d.averageRating DESC")
    List<Doctor> findTopRatedDoctorsBySpecialty(@Param("specialtyId") UUID specialtyId,
            @Param("status") DoctorStatus status);

    boolean existsByDoctorCode(String doctorCode);

    boolean existsByUserId(UUID userId);
}
