package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.DailyReport;
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
public interface DailyReportRepository extends JpaRepository<DailyReport, UUID> {

    Optional<DailyReport> findByReportDate(LocalDate reportDate);

    @Query("SELECT dr FROM DailyReport dr WHERE dr.reportDate BETWEEN :startDate AND :endDate " +
            "ORDER BY dr.reportDate DESC")
    List<DailyReport> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(dr.totalRevenue) FROM DailyReport dr " +
            "WHERE dr.reportDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(dr.totalAppointments) FROM DailyReport dr " +
            "WHERE dr.reportDate BETWEEN :startDate AND :endDate")
    Integer getTotalAppointmentsByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
