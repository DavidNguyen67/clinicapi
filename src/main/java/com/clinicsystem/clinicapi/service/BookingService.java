package com.clinicsystem.clinicapi.service;

import org.springframework.kafka.core.KafkaTemplate;
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
import com.clinicsystem.clinicapi.dto.QueueEventDto;
import com.clinicsystem.clinicapi.dto.UpdateAppointmentRequest;
import com.clinicsystem.clinicapi.model.Appointment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    @Qualifier("appointmentEventKafkaTemplate")
    private final KafkaTemplate<String, AppointmentEventDto> appointmentEventKafkaTemplate;
    @Qualifier("queueEventKafkaTemplate")
    private final KafkaTemplate<String, QueueEventDto> queueEventKafkaTemplate;
    private final AppointmentService appointmentService;

    @Transactional
    public AppointmentResponseDto publishAppointmentEvent(CreateAppointmentRequest request) {
        Appointment appointment = appointmentService.createAppointment(request);
        AppointmentEventDto appointmentEvent = buildEvent(appointment, AppointmentEventType.CREATED);

        QueueEventDto queueEvent = QueueEventDto.builder()
                .patientId(appointmentEvent.getPatientId())
                .patientName(appointment.getPatient().getUser().getFullName())
                .queueNumber(appointment.getQueueNumber())
                .status(appointment.getStatus())
                .doctorId(appointmentEvent.getDoctorId())
                .scheduledAt(appointmentEvent.getScheduledAt())
                .topic(KafkaTopics.QUEUE_EVENTS)
                .build();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                queueEventKafkaTemplate.send(KafkaTopics.QUEUE_EVENTS,
                        queueEvent.getPatientId().toString(),
                        queueEvent)
                        .whenComplete((result, ex) -> {
                            if (ex != null)
                                log.error("Kafka publish failed", ex);
                            else
                                log.info("Kafka published appointmentId={}", appointmentEvent.getAppointmentId());
                        });
                appointmentEventKafkaTemplate.send(KafkaTopics.APPOINTMENTS,
                        appointmentEvent.getPatientId().toString(),
                        appointmentEvent)
                        .whenComplete((result, ex) -> {
                            if (ex != null)
                                log.error("Kafka publish failed", ex);
                            else
                                log.info("Kafka published appointmentId={}", appointmentEvent.getAppointmentId());
                        });
            }
        });

        return toResponseDto(appointment);
    }

    @Transactional
    public AppointmentResponseDto updateAppointment(UpdateAppointmentRequest request) {
        Appointment appointment = appointmentService.updateAppointment(request);
        AppointmentEventDto event = buildEvent(appointment, AppointmentEventType.UPDATED);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                appointmentEventKafkaTemplate.send(KafkaTopics.APPOINTMENTS,
                        event.getPatientId().toString(),
                        event)
                        .whenComplete((result, ex) -> {
                            if (ex != null)
                                log.error("Kafka publish failed", ex);
                            else
                                log.info("Kafka published appointmentId={}", event.getAppointmentId());
                        });
            }
        });

        return toResponseDto(appointment);
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

    private AppointmentEventDto buildEvent(Appointment appointment, AppointmentEventType eventType) {
        AppointmentEventDto event = new AppointmentEventDto();
        event.setAppointmentId(appointment.getId());
        event.setPatientId(appointment.getPatient().getId());
        event.setDoctorId(appointment.getDoctor().getId());
        event.setScheduledAt(appointment.getAppointmentDate());
        event.setType(eventType);
        event.setEmail(appointment.getPatient().getUser().getEmail());
        event.setTopic(KafkaTopics.APPOINTMENTS);
        return event;
    }
}
