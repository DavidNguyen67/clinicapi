package com.clinicsystem.clinicapi.exception;

import com.clinicsystem.clinicapi.constant.MessageCode;

public class ResourceNotFoundException extends RuntimeException {

    private final String messageCode;
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String message) {
        super(message);
        this.messageCode = MessageCode.ERROR_NOT_FOUND;
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    public ResourceNotFoundException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s='%s'", resourceName, fieldName, fieldValue));
        this.messageCode = MessageCode.RESOURCE_NOT_FOUND;
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
