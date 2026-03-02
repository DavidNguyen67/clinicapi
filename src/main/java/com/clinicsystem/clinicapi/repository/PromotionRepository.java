package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    Optional<Promotion> findByCode(String code);

    List<Promotion> findByIsActiveTrue();

    @Query("SELECT p FROM Promotion p WHERE p.isActive = true " +
            "AND p.startDate <= :now AND p.endDate >= :now")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);

    @Query("SELECT p FROM Promotion p WHERE p.code = :code AND p.isActive = true " +
            "AND p.startDate <= :now AND p.endDate >= :now")
    Optional<Promotion> findValidPromotionByCode(@Param("code") String code, @Param("now") LocalDateTime now);

    @Query("SELECT p FROM Promotion p WHERE p.endDate < :now AND p.isActive = true")
    List<Promotion> findExpiredPromotions(@Param("now") LocalDateTime now);

    boolean existsByCode(String code);
}
