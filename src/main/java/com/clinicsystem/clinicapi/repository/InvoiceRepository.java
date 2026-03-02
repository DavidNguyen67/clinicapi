package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Invoice;
import com.clinicsystem.clinicapi.model.Invoice.InvoiceStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    Optional<Invoice> findByInvoiceCode(String invoiceCode);

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    List<Invoice> findByPatientId(UUID patientId);

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    Optional<Invoice> findByAppointmentId(UUID appointmentId);

    @EntityGraph(attributePaths = { "patient", "appointment", "items" })
    List<Invoice> findByStatus(InvoiceStatus status);

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    List<Invoice> findByPatientIdAndStatus(UUID patientId, InvoiceStatus status);

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId " +
            "ORDER BY i.invoiceDate DESC")
    List<Invoice> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @EntityGraph(attributePaths = { "patient", "appointment", "items" })
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    List<Invoice> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment" })
    @Query("SELECT i FROM Invoice i WHERE i.balance > 0 AND i.status != 'cancelled'")
    List<Invoice> findUnpaidInvoices();

    @EntityGraph(attributePaths = { "patient", "patient.user", "appointment", "items" })
    @Query("SELECT i FROM Invoice i WHERE i.patient.id = :patientId AND i.balance > 0 " +
            "AND i.status != 'cancelled'")
    List<Invoice> findUnpaidInvoicesByPatient(@Param("patientId") UUID patientId);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'paid' " +
            "AND i.invoiceDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(i.balance) FROM Invoice i WHERE i.status != 'cancelled'")
    BigDecimal getTotalOutstandingBalance();

    boolean existsByInvoiceCode(String invoiceCode);

    boolean existsByAppointmentId(UUID appointmentId);
}
