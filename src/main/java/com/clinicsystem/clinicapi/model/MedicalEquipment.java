package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_equipment", indexes = {
        @Index(name = "idx_code", columnList = "equipment_code"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_next_maintenance", columnList = "next_maintenance_date")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MedicalEquipment extends SoftDeletableEntity {

    @NotBlank(message = MessageCode.VALIDATION_EQUIPMENT_CODE_REQUIRED)
    @Column(name = "equipment_code", unique = true, nullable = false, length = 50)
    private String equipmentCode;

    @NotBlank(message = MessageCode.VALIDATION_NAME_REQUIRED)
    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(length = 255)
    private String manufacturer;

    @Column(length = 100)
    private String model;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "warranty_expiry")
    private LocalDateTime warrantyExpiry;

    @Column(length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EquipmentStatus status = EquipmentStatus.operational;

    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column(name = "maintenance_interval_days", nullable = false)
    private Integer maintenanceIntervalDays = 90;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public enum EquipmentStatus {
        operational, under_maintenance, broken, retired
    }

    // Helper method to calculate next maintenance
    public void updateNextMaintenance() {
        if (lastMaintenanceDate != null) {
            this.nextMaintenanceDate = lastMaintenanceDate.plusDays(maintenanceIntervalDays);
        }
    }
}
