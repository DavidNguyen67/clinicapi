package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.AppointmentEventType;
import com.clinicsystem.clinicapi.constant.AuthEventType;
import com.clinicsystem.clinicapi.constant.KafkaTopics;
import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.AuthEventDto;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.util.EmailCssInliner;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final HomeService homeService;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final NotificationService notificationService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Simple email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Inline CSS for email client compatibility
            String inlinedHtml = EmailCssInliner.inlineCSS(html);
            helper.setText(inlinedHtml, true); // true = HTML content

            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendPasswordResetEmail(String to, String resetToken, int expiryHours) {
        String subject = appName + " - Password Reset Request";

        // Create Thymeleaf context with variables
        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("resetToken", resetToken);
        context.setVariable("expiryHours", expiryHours);
        context.setVariable("resetLink", frontendUrl + "/reset-password?token=" + resetToken);

        // Process template
        String htmlContent = templateEngine.process("email/password-reset", context);

        sendHtmlEmail(to, subject, htmlContent);
        log.info("Password reset email sent to: {} with token valid for {} hours", to, expiryHours);
    }

    public void sendEmailVerification(String to, String verificationLink) {
        String subject = appName + " - Verify Your Email Address";

        // Create Thymeleaf context with variables
        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("verificationLink", verificationLink);

        // Process template
        String htmlContent = templateEngine.process("email/email-verification", context);

        sendHtmlEmail(to, subject, htmlContent);
        log.info("Email verification sent to: {}", to);
    }

    public void sendWelcomeEmail(String to, String fullName) {
        String subject = "Welcome to " + appName + "!";

        // Create Thymeleaf context with variables
        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("fullName", fullName);

        // Process template
        String htmlContent = templateEngine.process("email/welcome", context);

        sendHtmlEmail(to, subject, htmlContent);
        log.info("Welcome email sent to: {} ({})", to, fullName);
    }

    public void sendAppointmentNotification(String to, AppointmentEventDto event) {
        String subject = "Appointment Confirmation";

        // Create Thymeleaf context with variables
        ClinicInfoDto clinicInfo = homeService.getClinicInfo();

        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("patientName", to);
        context.setVariable("appointmentId", event.getAppointmentId());
        context.setVariable("scheduledAt", event.getScheduledAt());
        context.setVariable("doctorName", event.getDoctorId());
        context.setVariable("department", clinicInfo.getDescription());
        context.setVariable("clinicAddress", clinicInfo.getAddress());

        // Process template
        String htmlContent = templateEngine.process("email/appointment-confirmation", context);

        sendHtmlEmail(to, subject, htmlContent);
        log.info("Appointment confirmation email sent to: {}", to);
    }

    @KafkaListener(topics = KafkaTopics.APPOINTMENTS, groupId = "email-service-group", containerFactory = "appointmentKafkaListenerContainerFactory")
    public void handleAppointmentEvent(AppointmentEventDto event) {
        if (AppointmentEventType.CREATED.equals(event.getEventType())) {
            sendAppointmentNotification(event.getEmail(), event);
            notificationService.sendNotification(event.getPatientId().toString(), event);
        }
    }

    @KafkaListener(topics = KafkaTopics.AUTH_EVENTS, groupId = "email-service-group", containerFactory = "authKafkaListenerContainerFactory")
    public void handleAuthEvent(AuthEventDto event) {
        if (AuthEventType.REGISTER.equals(event.getEventType())) {
            sendWelcomeEmail(event.getEmail(), event.getFullName());
            notificationService.sendNotification(event.getUserId().toString(), event);
        }
    }
}
