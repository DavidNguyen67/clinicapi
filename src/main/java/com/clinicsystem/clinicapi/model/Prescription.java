package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions", indexes = {
        @Index(name = "idx_prescription_code", columnList = "prescription_code"),
        @Index(name = "idx_medical_record_id", columnList = "medical_record_id"),
        @Index(name = "idx_patient_id", columnList = "patient_id"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_status", columnList = "status")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Prescription extends SoftDeletableEntity {

    @NotBlank(message = MessageCode.VALIDATION_PRESCRIPTION_CODE_REQUIRED)
    @Column(name = "prescription_code", unique = true, nullable = false, length = 20)
    private String prescriptionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prescription_medical_record"))
    @NotNull(message = MessageCode.VALIDATION_MEDICAL_RECORD_REQUIRED)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prescription_patient"))
    @NotNull(message = MessageCode.VALIDATION_PATIENT_REQUIRED)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_prescription_doctor"))
    @NotNull(message = MessageCode.VALIDATION_DOCTOR_REQUIRED)
    private Doctor doctor;

    @NotNull(message = MessageCode.VALIDATION_PRESCRIPTION_DATE_REQUIRED)
    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrescriptionStatus status = PrescriptionStatus.active;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("prescription-items")
    private List<PrescriptionItem> items = new ArrayList<>();

    public enum PrescriptionStatus {
        active, dispensed, expired
    }

    // Helper methods
    public void addItem(PrescriptionItem item) {
        items.add(item);
        item.setPrescription(this);
    }

    public void removeItem(PrescriptionItem item) {
        items.remove(item);
        item.setPrescription(null);
    }
}
