package com.clinicsystem.clinicapi.service.appointment;

import com.clinicsystem.clinicapi.constant.KafkaTopics;
import com.clinicsystem.clinicapi.dto.AppointmentEventDto;
import com.clinicsystem.clinicapi.dto.QueueEventDto;
import com.clinicsystem.clinicapi.service.AppointmentService;
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
                .marshal(jacksonDataFormat)
                .setHeader(KafkaConstants.KEY,
                        simple("${header.patientId}"))
                .setHeader(KafkaConstants.TOPIC,
                        constant(KafkaTopics.QUEUE_EVENTS))
                .to("kafka:" + KafkaTopics.QUEUE_EVENTS)
                .log("Kafka published queueEvent patientId=${header.patientId}");

        from("direct:sendAppointmentEvent")
                .marshal(jacksonDataFormat)
                .setHeader(KafkaConstants.KEY,
                        simple("${header.patientId}"))
                .setHeader(KafkaConstants.TOPIC,
                        constant(KafkaTopics.APPOINTMENTS))
                .to("kafka:" + KafkaTopics.APPOINTMENTS)
                .log("Kafka published appointmentId=${header.appointmentId}");

        from("kafka:" + KafkaTopics.APPOINTMENTS +
                "?groupId=email-service-group")
                .unmarshal(jacksonDataFormat)
                .convertBodyTo(AppointmentEventDto.class)
                .filter(simple(
                        "${body.type} == 'CREATED' || ${body.type} == 'UPDATED'"))
                .log("Handling appointment event: ${body}")
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