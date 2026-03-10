package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.dto.SpecialtyTypeCountDto;
import com.clinicsystem.clinicapi.model.Specialty;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    int countByIsActive(Boolean isActive);

    @Query("""
            SELECT s.specialtyType AS specialtyType,
                   COUNT(d) AS count
            FROM Specialty s LEFT JOIN Doctor d ON s.id = d.specialty.id
            WHERE s.isActive = true
            GROUP BY s.specialtyType
            """)
    List<SpecialtyTypeCountDto> countGroupByType();

    @Query("SELECT s FROM Specialty s LEFT JOIN FETCH s.doctors")
    List<Specialty> findActiveForFirstPage(Pageable pageable);

    @Query("SELECT s FROM Specialty s LEFT JOIN FETCH s.doctors " +
            "WHERE s.createdAt < :cur")
    List<Specialty> getAllSpecialties(@Param("cur") LocalDateTime cur, Pageable pageable);
}
