package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    Optional<Medication> findByName(String name);

    List<Medication> findByCategory(String category);

    List<Medication> findByIsActiveTrue();

    List<Medication> findByCategoryAndIsActiveTrue(String category);

    @Query("SELECT m FROM Medication m WHERE " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.genericName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Medication> searchByNameOrGenericName(@Param("keyword") String keyword);

    @Query("SELECT m FROM Medication m WHERE m.isActive = true AND " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.genericName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Medication> searchActiveByNameOrGenericName(@Param("keyword") String keyword);

    boolean existsByName(String name);
}
