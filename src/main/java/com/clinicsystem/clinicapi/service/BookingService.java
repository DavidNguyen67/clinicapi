package com.clinicsystem.clinicapi.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.clinicsystem.clinicapi.constant.AppointmentEventType;
import com.clinicsystem.clinicapi.constant.KafkaTopics;
import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.AppointmentResponseDto;
import com.clinicsystem.clinicapi.dto.CreateAppointmentRequest;
import com.clinicsystem.clinicapi.model.Appointment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    @Qualifier("appointmentEventKafkaTemplate")
    private final KafkaTemplate<String, AppointmentEventDto> appointmentEventKafkaTemplate;
    private final EmailService emailService;
    private final AppointmentService appointmentService;

    @Transactional
    public AppointmentResponseDto publishAppointmentEvent(CreateAppointmentRequest createAppointmentRequest) {
        try {
            Appointment appointment = appointmentService.createAppointment(createAppointmentRequest);

            UUID appointmentId = appointment.getId();
            UUID patientId = appointment.getPatient().getId();
            String userEmail = appointment.getPatient().getUser().getEmail();

            AppointmentEventDto event = new AppointmentEventDto();
            event.setAppointmentId(appointment.getId());
            event.setPatientId(patientId);
            event.setDoctorId(appointment.getDoctor().getId());
            event.setScheduledAt(appointment.getAppointmentDate());
            event.setEventType(AppointmentEventType.CREATED);
            event.setEmail(userEmail);

            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                log.info(
                        "Appointment created. Scheduling Kafka publish after commit for appointmentId={}, patientId={}",
                        appointmentId,
                        patientId);

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        publishAppointmentCreatedEvent(appointmentId, patientId, event);
                    }

                    @Override
                    public void afterCompletion(int status) {
                        if (status != TransactionSynchronization.STATUS_COMMITTED) {
                            log.warn(
                                    "Transaction rolled back. Kafka event will not be published for appointmentId={}, patientId={}",
                                    appointmentId,
                                    patientId);
                        }
                    }
                });
            } else {
                log.warn(
                        "No active transaction. Publishing Kafka event immediately for appointmentId={}, patientId={}",
                        appointmentId,
                        patientId);
                publishAppointmentCreatedEvent(appointmentId, patientId, event);
            }

            return toResponseDto(appointment);
        } catch (Exception e) {
            log.error("Error occurred while publishing appointment event", e);
            throw e;
        }
    }

    private void publishAppointmentCreatedEvent(UUID appointmentId, UUID patientId, AppointmentEventDto event) {
        CompletableFuture<SendResult<String, AppointmentEventDto>> future = appointmentEventKafkaTemplate.send(
                KafkaTopics.APPOINTMENTS,
                patientId.toString(),
                event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka event published after commit for appointmentId={}, patientId={}",
                        appointmentId,
                        patientId);
            } else {
                log.error("Failed to publish Kafka event for appointmentId={}, patientId={}",
                        appointmentId,
                        patientId,
                        ex);
            }
        });
    }

    @KafkaListener(topics = KafkaTopics.APPOINTMENTS, groupId = "email-service-group")
    public void handleAppointmentEvent(AppointmentEventDto event) {
        log.info("Received Kafka event for appointmentId={}, patientId={}, eventType={}",
                event.getAppointmentId(),
                event.getPatientId(),
                event.getEventType());
        if (AppointmentEventType.CREATED.equals(event.getEventType())) {
            log.info("Processing appointment created event for appointmentId={}, patientId={}",
                    event.getAppointmentId(),
                    event.getPatientId());
            emailService.sendAppointmentNotification(event.getEmail(), event);
        }
    }

    private AppointmentResponseDto toResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .appointmentCode(appointment.getAppointmentCode())
                .patientId(appointment.getPatient() != null ? appointment.getPatient().getId() : null)
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                .serviceId(appointment.getService() != null ? appointment.getService().getId() : null)
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .bookingType(appointment.getBookingType())
                .reason(appointment.getReason())
                .symptoms(appointment.getSymptoms())
                .notes(appointment.getNotes())
                .queueNumber(appointment.getQueueNumber())
                .build();
    }
}
