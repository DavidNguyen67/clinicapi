package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.EquipmentMaintenance;
import com.clinicsystem.clinicapi.model.EquipmentMaintenance.MaintenanceStatus;
import com.clinicsystem.clinicapi.model.EquipmentMaintenance.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EquipmentMaintenanceRepository extends JpaRepository<EquipmentMaintenance, UUID> {

    List<EquipmentMaintenance> findByEquipmentId(UUID equipmentId);

    List<EquipmentMaintenance> findByStatus(MaintenanceStatus status);

    List<EquipmentMaintenance> findByMaintenanceType(MaintenanceType maintenanceType);

    @Query("SELECT em FROM EquipmentMaintenance em WHERE em.equipment.id = :equipmentId " +
            "ORDER BY em.scheduledDate DESC")
    List<EquipmentMaintenance> findByEquipmentIdOrderByDateDesc(@Param("equipmentId") UUID equipmentId);

    @Query("SELECT em FROM EquipmentMaintenance em WHERE em.scheduledDate = :date " +
            "AND em.status IN ('scheduled', 'in_progress')")
    List<EquipmentMaintenance> findScheduledMaintenanceByDate(@Param("date") LocalDate date);

    @Query("SELECT em FROM EquipmentMaintenance em WHERE em.scheduledDate BETWEEN :startDate AND :endDate")
    List<EquipmentMaintenance> findByScheduledDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
