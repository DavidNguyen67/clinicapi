package com.clinicsystem.clinicapi.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentEventType {
    CREATED("appointment.created"),
    UPDATED("appointment.updated"),
    CANCELED("appointment.canceled"),
    COMPLETED("appointment.completed");

    private final String value;

    AppointmentEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
