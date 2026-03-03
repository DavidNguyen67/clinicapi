package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "doctor_performance", uniqueConstraints = @UniqueConstraint(name = "unique_doctor_month", columnNames = {
                "doctor_id", "month", "year" }), indexes = {
                                @Index(name = "idx_doctor", columnList = "doctor_id"),
                                @Index(name = "idx_period", columnList = "year, month")
                })
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DoctorPerformance extends SoftDeletableEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_performance_doctor"))
        @NotNull(message = MessageCode.VALIDATION_DOCTOR_REQUIRED)
        private Doctor doctor;

        @NotNull(message = MessageCode.VALIDATION_MONTH_REQUIRED)
        @Min(value = 1, message = MessageCode.VALIDATION_MONTH_RANGE)
        @Max(value = 12, message = MessageCode.VALIDATION_MONTH_RANGE)
        @Column(nullable = false)
        private Integer month;

        @NotNull(message = MessageCode.VALIDATION_YEAR_REQUIRED)
        @Column(nullable = false)
        private Integer year;

        @Column(name = "total_appointments", nullable = false)
        private Integer totalAppointments = 0;

        @Column(name = "completed_appointments", nullable = false)
        private Integer completedAppointments = 0;

        @Column(name = "cancelled_appointments", nullable = false)
        private Integer cancelledAppointments = 0;

        @Column(name = "total_patients", nullable = false)
        private Integer totalPatients = 0;

        @Column(name = "average_rating", precision = 3, scale = 2)
        private BigDecimal averageRating;

        @Column(name = "total_revenue", nullable = false, precision = 12, scale = 2)
        private BigDecimal totalRevenue = BigDecimal.ZERO;
}
