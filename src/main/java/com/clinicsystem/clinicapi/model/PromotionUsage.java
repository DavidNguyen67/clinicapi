package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_usage", indexes = {
        @Index(name = "idx_promotion", columnList = "promotion_id"),
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_invoice", columnList = "invoice_id")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PromotionUsage extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_usage_promotion"))
    @NotNull(message = MessageCode.VALIDATION_PROMOTION_REQUIRED)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_usage_user"))
    @NotNull(message = MessageCode.VALIDATION_USER_REQUIRED)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_usage_invoice"))
    private Invoice invoice;

    @NotNull(message = MessageCode.VALIDATION_DISCOUNT_AMOUNT_REQUIRED)
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "used_at", nullable = false)
    private LocalDateTime usedAt;

    @PrePersist
    protected void onCreateUsage() {
        if (usedAt == null) {
            usedAt = LocalDateTime.now();
        }
    }
}
