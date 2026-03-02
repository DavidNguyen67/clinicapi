package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, UUID> {

    List<PromotionUsage> findByPromotionId(UUID promotionId);

    List<PromotionUsage> findByUserId(UUID userId);

    List<PromotionUsage> findByInvoiceId(UUID invoiceId);

    @Query("SELECT COUNT(pu) FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId " +
            "AND pu.user.id = :userId")
    Long countByPromotionAndUser(@Param("promotionId") UUID promotionId, @Param("userId") UUID userId);

    @Query("SELECT pu FROM PromotionUsage pu WHERE pu.usedAt BETWEEN :startDate AND :endDate")
    List<PromotionUsage> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
