package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Review;
import com.clinicsystem.clinicapi.model.Review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByPatientId(UUID patientId);

    List<Review> findByDoctorId(UUID doctorId);

    Optional<Review> findByAppointmentId(UUID appointmentId);

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByDoctorIdAndStatus(UUID doctorId, ReviewStatus status);

    @Query("SELECT r FROM Review r WHERE r.doctor.id = :doctorId AND r.status = 'approved' " +
            "ORDER BY r.createdAt DESC")
    List<Review> findApprovedReviewsByDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT r FROM Review r WHERE r.patient.id = :patientId " +
            "ORDER BY r.createdAt DESC")
    List<Review> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId " +
            "AND r.status = 'approved'")
    Double getAverageRatingByDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId " +
            "AND r.status = 'approved'")
    Long getApprovedReviewCountByDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId " +
            "AND r.status = 'approved' GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingDistributionByDoctor(@Param("doctorId") UUID doctorId);

    boolean existsByAppointmentId(UUID appointmentId);

    boolean existsByPatientIdAndDoctorId(UUID patientId, UUID doctorId);
}
