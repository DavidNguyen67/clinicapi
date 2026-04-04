package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {
    private UUID id;
    private String appointmentCode;
    private UUID patientId;
    private UUID doctorId;
    private UUID serviceId;
    private LocalDateTime appointmentDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Appointment.AppointmentStatus status;
    private Appointment.BookingType bookingType;
    private String reason;
    private String symptoms;
    private String notes;
    private Integer queueNumber;
}
