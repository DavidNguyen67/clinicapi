package com.clinicsystem.clinicapi.service.appointment;

import com.clinicsystem.clinicapi.constant.AppointmentEventType;
import com.clinicsystem.clinicapi.constant.KafkaTopics;
import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.QueueEventDto;
import com.clinicsystem.clinicapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentRoutes extends RouteBuilder {
    private final EmailService emailService;
    private final JacksonDataFormat jacksonDataFormat;

    @Override
    public void configure() {
        onException(Exception.class)
                .log("ERROR in route ${routeId}: ${exception.message}")
                .log("Caused by: ${exception.stacktrace}")
                .handled(true);

        from("direct:publishAppointmentEvent")
                .transacted()
                .process(exchange -> {
                    AppointmentEventDto event = exchange.getIn()
                            .getBody(AppointmentEventDto.class);
                    QueueEventDto queueEvent = exchange.getIn()
                            .getHeader("queueEvent", QueueEventDto.class);

                    exchange.getIn().setHeader("patientId",
                            event.getPatientId().toString());
                })
                .multicast().parallelProcessing()
                .to("direct:sendQueueEvent")
                .to("direct:sendAppointmentEvent")
                .end();

        from("direct:sendQueueEvent")
                .setHeader(KafkaConstants.KEY,
                        simple("${header.patientId}"))
                .setHeader(KafkaConstants.TOPIC,
                        constant(KafkaTopics.QUEUE_EVENTS))
                .marshal(jacksonDataFormat)
                .to("kafka:" + KafkaTopics.QUEUE_EVENTS)
                .log("Kafka published queueEvent patientId=${header.appointmentId}");

        from("direct:sendAppointmentEvent")
                .setHeader(KafkaConstants.KEY,
                        simple("${header.patientId}"))
                .setHeader(KafkaConstants.TOPIC,
                        constant(KafkaTopics.APPOINTMENT_EVENT))
                .marshal(jacksonDataFormat)
                .to("kafka:" + KafkaTopics.APPOINTMENT_EVENT)
                .log("Kafka published appointmentId=${header.appointmentId}");

        from("kafka:" + KafkaTopics.APPOINTMENT_EVENT + "?groupId=email-service-group")
                .unmarshal(jacksonDataFormat)
                .filter(exchange -> {
                    AppointmentEventType type = exchange.getIn().getBody(AppointmentEventDto.class).getType();
                    return type == AppointmentEventType.CREATED || type == AppointmentEventType.UPDATED;
                })
                .log("Handling appointment event: ${body}")
                .to("direct:sendEmailNotification");

        from("kafka:" + KafkaTopics.QUEUE_EVENTS + "?groupId=email-service-group")
                .unmarshal(jacksonDataFormat)
                .log("Handling queue event: ${body}")
                .to("direct:sendEmailNotification");

        from("direct:sendEmailNotification")
                .process(exchange -> {
                    AppointmentEventDto event = exchange.getIn()
                            .getBody(AppointmentEventDto.class);
                    exchange.getIn().setHeader("recipientEmail", event.getEmail());
                })
                .bean(emailService, "sendAppointmentNotification(${header.recipientEmail}, ${body})");
    }
}