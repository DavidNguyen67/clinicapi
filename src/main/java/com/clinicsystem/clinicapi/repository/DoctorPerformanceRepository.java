package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.DoctorPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorPerformanceRepository extends JpaRepository<DoctorPerformance, UUID> {

    Optional<DoctorPerformance> findByDoctorIdAndMonthAndYear(UUID doctorId, Integer month, Integer year);

    List<DoctorPerformance> findByDoctorId(UUID doctorId);

    List<DoctorPerformance> findByYear(Integer year);

    List<DoctorPerformance> findByMonthAndYear(Integer month, Integer year);

    @Query("SELECT dp FROM DoctorPerformance dp WHERE dp.doctor.id = :doctorId " +
            "ORDER BY dp.year DESC, dp.month DESC")
    List<DoctorPerformance> findByDoctorIdOrderByPeriodDesc(@Param("doctorId") UUID doctorId);

    @Query("SELECT dp FROM DoctorPerformance dp WHERE dp.year = :year AND dp.month = :month " +
            "ORDER BY dp.totalRevenue DESC")
    List<DoctorPerformance> findTopPerformersByMonth(
            @Param("year") Integer year,
            @Param("month") Integer month);
}
