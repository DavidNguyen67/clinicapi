package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.AppointmentEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEventDto extends BaseEventDto {
    private AppointmentEventType type;
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime scheduledAt;
    private String email;
}
