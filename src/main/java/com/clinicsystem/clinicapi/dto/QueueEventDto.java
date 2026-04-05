package com.clinicsystem.clinicapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.clinicsystem.clinicapi.model.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueEventDto {
    private String topic;
    private UUID patientId;
    private String patientName;
    private int queueNumber;
    private Appointment.AppointmentStatus status;
    private UUID doctorId;
    private LocalDateTime scheduledAt;
}
