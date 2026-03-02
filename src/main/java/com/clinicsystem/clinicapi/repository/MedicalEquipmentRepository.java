package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.MedicalEquipment;
import com.clinicsystem.clinicapi.model.MedicalEquipment.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalEquipmentRepository extends JpaRepository<MedicalEquipment, UUID> {

    Optional<MedicalEquipment> findByEquipmentCode(String equipmentCode);

    List<MedicalEquipment> findByStatus(EquipmentStatus status);

    List<MedicalEquipment> findByCategory(String category);

    List<MedicalEquipment> findByLocation(String location);

    @Query("SELECT me FROM MedicalEquipment me WHERE me.nextMaintenanceDate <= :date " +
            "AND me.status = 'operational'")
    List<MedicalEquipment> findDueForMaintenance(@Param("date") LocalDate date);

    @Query("SELECT me FROM MedicalEquipment me WHERE me.warrantyExpiry <= :date " +
            "AND me.status != 'retired'")
    List<MedicalEquipment> findExpiringWarranty(@Param("date") LocalDate date);

    boolean existsByEquipmentCode(String equipmentCode);
}
