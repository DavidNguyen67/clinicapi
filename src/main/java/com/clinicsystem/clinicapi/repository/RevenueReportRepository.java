package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.RevenueReport;
import com.clinicsystem.clinicapi.model.RevenueReport.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RevenueReportRepository extends JpaRepository<RevenueReport, UUID> {

    List<RevenueReport> findByReportType(ReportType reportType);

    List<RevenueReport> findByReportDate(LocalDate reportDate);

    @Query("SELECT rr FROM RevenueReport rr WHERE rr.reportType = :type " +
            "ORDER BY rr.reportDate DESC")
    List<RevenueReport> findByTypeOrderByDateDesc(@Param("type") ReportType type);

    @Query("SELECT rr FROM RevenueReport rr WHERE rr.reportDate BETWEEN :startDate AND :endDate " +
            "ORDER BY rr.reportDate DESC")
    List<RevenueReport> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
