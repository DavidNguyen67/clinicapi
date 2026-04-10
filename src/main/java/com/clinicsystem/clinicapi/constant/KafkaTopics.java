package com.clinicsystem.clinicapi.constant;

public class KafkaTopics {
    public static final String APPOINTMENT_EVENT = "clinic.appointment-events";

    public static final String AUTH_EVENTS = "clinic.auth-events";

    public static final String QUEUE_EVENTS = "clinic.queue-events";

    private KafkaTopics() {
    }
}
