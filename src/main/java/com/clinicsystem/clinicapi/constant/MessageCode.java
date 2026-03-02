package com.clinicsystem.clinicapi.constant;

public class MessageCode {
    // General messages
    public static final String GENERAL_SUCCESS = "GENERAL_SUCCESS";
    public static final String GENERAL_ERROR = "GENERAL_ERROR";
    public static final String GENERAL_CREATED = "GENERAL_CREATED";
    public static final String GENERAL_UPDATED = "GENERAL_UPDATED";
    public static final String GENERAL_DELETED = "GENERAL_DELETED";

    // Error messages
    public static final String ERROR_VALIDATION = "ERROR_VALIDATION";
    public static final String ERROR_NOT_FOUND = "ERROR_NOT_FOUND";
    public static final String ERROR_BAD_REQUEST = "ERROR_BAD_REQUEST";
    public static final String ERROR_UNAUTHORIZED = "ERROR_UNAUTHORIZED";
    public static final String ERROR_FORBIDDEN = "ERROR_FORBIDDEN";
    public static final String ERROR_INTERNAL = "ERROR_INTERNAL";
    public static final String ERROR_BAD_CREDENTIALS = "ERROR_BAD_CREDENTIALS";

    // Authentication & Authorization
    public static final String AUTH_LOGIN_SUCCESS = "AUTH_LOGIN_SUCCESS";
    public static final String AUTH_LOGOUT_SUCCESS = "AUTH_LOGOUT_SUCCESS";
    public static final String AUTH_REGISTER_SUCCESS = "AUTH_REGISTER_SUCCESS";
    public static final String AUTH_TOKEN_INVALID = "AUTH_TOKEN_INVALID";
    public static final String AUTH_TOKEN_EXPIRED = "AUTH_TOKEN_EXPIRED";
    public static final String AUTH_REFRESH_SUCCESS = "AUTH_REFRESH_SUCCESS";
    public static final String AUTH_REFRESH_TOKEN_INVALID = "AUTH_REFRESH_TOKEN_INVALID";
    public static final String AUTH_REFRESH_TOKEN_EXPIRED = "AUTH_REFRESH_TOKEN_EXPIRED";

    // API Key messages
    public static final String API_KEY_MISSING = "API_KEY_MISSING";
    public static final String API_KEY_INVALID = "API_KEY_INVALID";

    // User messages
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String USER_CREATED = "USER_CREATED";
    public static final String USER_UPDATED = "USER_UPDATED";
    public static final String USER_DELETED = "USER_DELETED";
    public static final String USER_EMAIL_ALREADY_EXISTS = "USER_EMAIL_ALREADY_EXISTS";
    public static final String USER_PHONE_ALREADY_EXISTS = "USER_PHONE_ALREADY_EXISTS";
    public static final String USER_INVALID_CREDENTIALS = "USER_INVALID_CREDENTIALS";
    public static final String USER_ACCOUNT_INACTIVE = "USER_ACCOUNT_INACTIVE";

    // Password messages
    public static final String PASSWORD_CHANGED_SUCCESS = "PASSWORD_CHANGED_SUCCESS";
    public static final String PASSWORD_CURRENT_INCORRECT = "PASSWORD_CURRENT_INCORRECT";
    public static final String PASSWORD_MISMATCH = "PASSWORD_MISMATCH";
    public static final String PASSWORD_RESET_EMAIL_SENT = "PASSWORD_RESET_EMAIL_SENT";
    public static final String PASSWORD_RESET_SUCCESS = "PASSWORD_RESET_SUCCESS";
    public static final String PASSWORD_RESET_TOKEN_INVALID = "PASSWORD_RESET_TOKEN_INVALID";
    public static final String PASSWORD_RESET_TOKEN_EXPIRED = "PASSWORD_RESET_TOKEN_EXPIRED";
    public static final String PASSWORD_RESET_TOKEN_USED = "PASSWORD_RESET_TOKEN_USED";

    // Validation messages
    public static final String VALIDATION_USERNAME_REQUIRED = "VALIDATION_USERNAME_REQUIRED";
    public static final String VALIDATION_USERNAME_SIZE = "VALIDATION_USERNAME_SIZE";
    public static final String VALIDATION_EMAIL_REQUIRED = "VALIDATION_EMAIL_REQUIRED";
    public static final String VALIDATION_EMAIL_INVALID = "VALIDATION_EMAIL_INVALID";
    public static final String VALIDATION_PASSWORD_REQUIRED = "VALIDATION_PASSWORD_REQUIRED";
    public static final String VALIDATION_PASSWORD_SIZE = "VALIDATION_PASSWORD_SIZE";

    // Resource messages
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    // Patient messages
    public static final String PATIENT_NOT_FOUND = "PATIENT_NOT_FOUND";
    public static final String PATIENT_ALREADY_EXISTS = "PATIENT_ALREADY_EXISTS";
    public static final String PATIENT_CREATED = "PATIENT_CREATED";
    public static final String PATIENT_UPDATED = "PATIENT_UPDATED";
    public static final String PATIENT_DELETED = "PATIENT_DELETED";

    // Doctor messages
    public static final String DOCTOR_NOT_FOUND = "DOCTOR_NOT_FOUND";
    public static final String DOCTOR_CREATED = "DOCTOR_CREATED";
    public static final String DOCTOR_UPDATED = "DOCTOR_UPDATED";
    public static final String DOCTOR_DELETED = "DOCTOR_DELETED";

    // Service messages
    public static final String SERVICE_NOT_FOUND = "SERVICE_NOT_FOUND";
    public static final String SERVICE_CREATED = "SERVICE_CREATED";
    public static final String SERVICE_UPDATED = "SERVICE_UPDATED";
    public static final String SERVICE_DELETED = "SERVICE_DELETED";

    // Specialty messages
    public static final String SPECIALTY_NOT_FOUND = "SPECIALTY_NOT_FOUND";
    public static final String SPECIALTY_CREATED = "SPECIALTY_CREATED";
    public static final String SPECIALTY_UPDATED = "SPECIALTY_UPDATED";
    public static final String SPECIALTY_DELETED = "SPECIALTY_DELETED";

    // System messages
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

    private MessageCode() {
        // Private constructor to prevent instantiation
    }
}
