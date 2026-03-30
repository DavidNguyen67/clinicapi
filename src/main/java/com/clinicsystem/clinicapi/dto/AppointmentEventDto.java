package com.clinicsystem.clinicapi.dto;

import java.time.LocalDate;
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
    private LocalDate scheduledAt;
    private String email;
}
