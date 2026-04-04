package com.clinicsystem.clinicapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.clinicsystem.clinicapi.constant.AppointmentEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEventDto {
    private AppointmentEventType eventType;
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime scheduledAt;
    private String email;
}
