package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Payment;
import com.clinicsystem.clinicapi.model.Payment.PaymentMethod;
import com.clinicsystem.clinicapi.model.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByPaymentCode(String paymentCode);

    List<Payment> findByInvoiceId(UUID invoiceId);

    List<Payment> findByPatientId(UUID patientId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> findByPatientIdAndStatus(UUID patientId, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.patient.id = :patientId " +
            "ORDER BY p.paymentDate DESC")
    List<Payment> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.status = 'completed' " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findCompletedPaymentsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'completed' " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalPaymentsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.paymentMethod, SUM(p.amount) FROM Payment p " +
            "WHERE p.status = 'completed' AND p.paymentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.paymentMethod")
    List<Object[]> getPaymentsByMethodAndDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    boolean existsByPaymentCode(String paymentCode);
}
