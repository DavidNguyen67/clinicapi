package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Inventory;
import com.clinicsystem.clinicapi.model.Inventory.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    List<Inventory> findByMedicationId(UUID medicationId);

    Optional<Inventory> findByBatchNumber(String batchNumber);

    List<Inventory> findByStatus(InventoryStatus status);

    List<Inventory> findByMedicationIdAndStatus(UUID medicationId, InventoryStatus status);

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.alertThreshold AND i.status != 'expired'")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate <= :date AND i.status != 'expired'")
    List<Inventory> findExpiringItems(@Param("date") LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate BETWEEN :startDate AND :endDate")
    List<Inventory> findByExpiryDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.medication.id = :medicationId " +
            "AND i.status IN ('in_stock', 'low_stock')")
    Integer getTotalQuantityByMedicationId(@Param("medicationId") UUID medicationId);

    boolean existsByBatchNumber(String batchNumber);
}
