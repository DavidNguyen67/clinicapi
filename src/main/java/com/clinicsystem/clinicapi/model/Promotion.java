package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions", indexes = {
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_dates", columnList = "start_date, end_date"),
        @Index(name = "idx_active", columnList = "is_active")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Promotion extends SoftDeletableEntity {

    @NotBlank(message = MessageCode.VALIDATION_PROMOTION_CODE_REQUIRED)
    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @NotBlank(message = MessageCode.VALIDATION_NAME_REQUIRED)
    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    @NotNull(message = MessageCode.VALIDATION_DISCOUNT_TYPE_REQUIRED)
    private DiscountType discountType;

    @NotNull(message = MessageCode.VALIDATION_DISCOUNT_VALUE_REQUIRED)
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "min_purchase_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal minPurchaseAmount = BigDecimal.ZERO;

    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_count", nullable = false)
    private Integer usageCount = 0;

    @Column(name = "usage_per_user", nullable = false)
    private Integer usagePerUser = 1;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applicable_services", columnDefinition = "JSON")
    private List<String> applicableServices;

    @NotNull(message = MessageCode.VALIDATION_START_DATE_REQUIRED)
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = MessageCode.VALIDATION_END_DATE_REQUIRED)
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public enum DiscountType {
        percentage, fixed_amount
    }

    // Helper methods
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && now.isAfter(startDate) && now.isBefore(endDate) &&
                (usageLimit == null || usageCount < usageLimit);
    }

    public void incrementUsage() {
        this.usageCount++;
    }
}
