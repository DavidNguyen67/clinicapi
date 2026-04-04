package com.clinicsystem.clinicapi.model;

import java.time.LocalDateTime;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loyalty_transactions", indexes = {
        @Index(name = "idx_patient", columnList = "patient_id"),
        @Index(name = "idx_type", columnList = "transaction_type"),
        @Index(name = "idx_created", columnList = "created_at")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoyaltyTransaction extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_loyalty_patient"))
    @NotNull(message = MessageCode.VALIDATION_PATIENT_REQUIRED)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    @NotNull(message = MessageCode.VALIDATION_TRANSACTION_TYPE_REQUIRED)
    private TransactionType transactionType;

    @NotNull(message = MessageCode.VALIDATION_POINTS_REQUIRED)
    @Column(nullable = false)
    private Integer points;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = MessageCode.VALIDATION_BALANCE_AFTER_REQUIRED)
    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum TransactionType {
        earn, redeem, expire, adjustment
    }
}
