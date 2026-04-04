package com.clinicsystem.clinicapi.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthEventType {
    REGISTER("auth.register"),
    FORGOT_PASSWORD("auth.forgot_password"),
    LOGIN("auth.login");

    private final String value;

    AuthEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
