//package com.clinicsystem.clinicapi.service;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import com.clinicsystem.clinicapi.constant.AppointmentEventType;
//import com.clinicsystem.clinicapi.constant.AuthEventType;
//import com.clinicsystem.clinicapi.constant.KafkaTopics;
//import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
//import com.clinicsystem.clinicapi.dto.AuthEventDto;
//import com.clinicsystem.clinicapi.dto.QueueEventDto;
//import com.clinicsystem.clinicapi.model.User;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.messaging.handler.annotation.Header;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KafkaListenerService {
//    private final EmailService emailService;
//    private final ObjectMapper objectMapper;
//    private final NotificationService notificationService;
//
//    @KafkaListener(topics = KafkaTopics.APPOINTMENTS, groupId = "email-service-group", containerFactory = "appointmentKafkaListenerContainerFactory")
//    public void handleAppointmentEvent(AppointmentEventDto event) {
//        if (AppointmentEventType.CREATED.equals(event.getType())
//                || AppointmentEventType.UPDATED.equals(event.getType())) {
//            log.info("Handling appointment event for email notification: {}", event);
//            emailService.sendAppointmentNotification(event.getEmail(), event);
//        }
//    }
//
//    @KafkaListener(topics = KafkaTopics.AUTH_EVENTS, groupId = "email-service-group", containerFactory = "authKafkaListenerContainerFactory")
//    public void handleAuthEvent(AuthEventDto event) {
//        if (AuthEventType.REGISTER.equals(event.getEventType())) {
//            emailService.sendWelcomeEmail(event.getEmail(), event.getFullName());
//        }
//        if (AuthEventType.FORGOT_PASSWORD.equals(event.getEventType())) {
//            User user = event.getUser();
//            log.info("Handling forgot password event for user: {}", user.getEmail());
//            emailService.sendPasswordResetEmail(user.getEmail(), event.getResetToken(), event.getExpiryHours());
//        }
//        if (AuthEventType.LOGIN.equals(event.getEventType())) {
//            User user = event.getUser();
//            log.info("Handling login event for user: {}", user.getEmail());
//        }
//    }
//
//    @KafkaListener(topics = { KafkaTopics.APPOINTMENTS,
//            KafkaTopics.AUTH_EVENTS,
//            KafkaTopics.QUEUE_EVENTS }, groupId = "in-app-service-group", containerFactory = "stringKafkaListenerContainerFactory")
//    public void handleForInApp(
//            @Payload String rawPayload,
//            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JsonMappingException, JsonProcessingException {
//        if (topic.equals(KafkaTopics.APPOINTMENTS)) {
//            log.info("Received appointment event for in-app notification: {}", rawPayload);
//            AppointmentEventDto event = objectMapper.readValue(rawPayload, AppointmentEventDto.class);
//            notificationService.sendNotification(event.getPatientId().toString(), event);
//        }
//        if (topic.equals(KafkaTopics.AUTH_EVENTS)) {
//            log.info("Received auth event for in-app notification: {}", rawPayload);
//            AuthEventDto event = objectMapper.readValue(rawPayload, AuthEventDto.class);
//            notificationService.sendNotification(event.getUser().getId().toString(), event);
//        }
//        if (topic.equals(KafkaTopics.QUEUE_EVENTS)) {
//            log.info("Received queue event for in-app notification: {}", rawPayload);
//            QueueEventDto event = objectMapper.readValue(rawPayload, QueueEventDto.class);
//            notificationService.sendNotification(event.getPatientId().toString(), event);
//        }
//    }
//
//}
