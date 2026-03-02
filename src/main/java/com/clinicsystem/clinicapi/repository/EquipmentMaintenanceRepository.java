package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.EquipmentMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EquipmentMaintenanceRepository extends JpaRepository<EquipmentMaintenance, UUID> {

}
