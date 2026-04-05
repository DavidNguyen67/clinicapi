package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.model.Appointment;
import com.clinicsystem.clinicapi.validation.Normalize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.clinicsystem.clinicapi.validation.Normalize.NormalizeType.TRIM;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    @NotNull(message = MessageCode.VALIDATION_PATIENT_REQUIRED)
    private UUID patientId;

    @NotNull(message = MessageCode.VALIDATION_DOCTOR_REQUIRED)
    private UUID doctorId;

    private UUID serviceId;

    @NotNull(message = MessageCode.VALIDATION_APPOINTMENT_DATE_REQUIRED)
    private LocalDateTime appointmentDate;

    @NotNull(message = MessageCode.VALIDATION_START_TIME_REQUIRED)
    private LocalDateTime startTime;

    @NotNull(message = MessageCode.VALIDATION_END_TIME_REQUIRED)
    private LocalDateTime endTime;

    private Appointment.BookingType bookingType;

    @Normalize(TRIM)
    private String reason;

    @Normalize(TRIM)
    private String symptoms;

    @Normalize(TRIM)
    private String notes;

}
