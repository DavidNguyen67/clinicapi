package com.clinicsystem.clinicapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.clinicsystem.clinicapi.model.Appointment;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QueueEventDto extends BaseEventDto {
    private UUID patientId;
    private String patientName;
    private int queueNumber;
    private Appointment.AppointmentStatus status;
    private UUID doctorId;
    private LocalDateTime scheduledAt;
}
