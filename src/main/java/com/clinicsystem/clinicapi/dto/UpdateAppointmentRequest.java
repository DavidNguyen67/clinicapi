package com.clinicsystem.clinicapi.dto;

import java.util.UUID;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.model.Appointment.AppointmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {
    @NotNull(message = MessageCode.VALIDATION_APPOINTMENT_REQUIRED)
    private UUID appointmentId;

    @NotNull(message = MessageCode.VALIDATION_APPOINTMENT_STATUS_REQUIRED)
    private AppointmentStatus status;
}
