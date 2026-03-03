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
import java.time.LocalDate;

@Entity
@Table(name = "revenue_reports", indexes = {
        @Index(name = "idx_report_date", columnList = "report_date"),
        @Index(name = "idx_type", columnList = "report_type")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RevenueReport extends SoftDeletableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 20)
    @NotNull(message = MessageCode.VALIDATION_REPORT_TYPE_REQUIRED)
    private ReportType reportType;

    @NotNull(message = MessageCode.VALIDATION_REPORT_DATE_REQUIRED)
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @NotNull(message = MessageCode.VALIDATION_START_DATE_REQUIRED)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = MessageCode.VALIDATION_END_DATE_REQUIRED)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "service_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal serviceRevenue = BigDecimal.ZERO;

    @Column(name = "medication_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal medicationRevenue = BigDecimal.ZERO;

    @Column(name = "lab_test_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal labTestRevenue = BigDecimal.ZERO;

    @Column(name = "total_appointments", nullable = false)
    private Integer totalAppointments = 0;

    @Column(name = "total_patients", nullable = false)
    private Integer totalPatients = 0;

    public enum ReportType {
        daily, weekly, monthly, yearly
    }
}
