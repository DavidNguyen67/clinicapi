package com.clinicsystem.clinicapi.model;

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

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "medical_records", indexes = {
        @Index(name = "idx_record_code", columnList = "record_code"),
        @Index(name = "idx_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_patient_id", columnList = "patient_id"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        // Composite indexes for common queries
        @Index(name = "idx_patient_created", columnList = "patient_id, created_at"),
        @Index(name = "idx_doctor_created", columnList = "doctor_id, created_at")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MedicalRecord extends SoftDeletableEntity {

    @NotBlank(message = "Record code is required")
    @Column(name = "record_code", unique = true, nullable = false, length = 20)
    private String recordCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "fk_record_appointment"))
    @NotNull(message = "Appointment is required")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_record_patient"))
    @NotNull(message = "Patient is required")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_record_doctor"))
    @NotNull(message = "Doctor is required")
    private Doctor doctor;

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "vital_signs", columnDefinition = "JSON")
    private Map<String, Object> vitalSigns;

    @NotBlank(message = "Diagnosis is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;
}
