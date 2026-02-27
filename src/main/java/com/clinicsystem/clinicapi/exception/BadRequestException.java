package com.clinicsystem.clinicapi.exception;

import com.clinicsystem.clinicapi.constant.MessageCode;

public class BadRequestException extends RuntimeException {

    private final String messageCode;

    public BadRequestException(String message) {
        super(message);
        this.messageCode = MessageCode.ERROR_BAD_REQUEST;
    }

    public BadRequestException(String message, String messageCode) {
        super(message);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
