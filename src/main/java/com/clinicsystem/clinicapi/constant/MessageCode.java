package com.clinicsystem.clinicapi.constant;

public class MessageCode {

    // General messages
    public static final String SUCCESS = "general.success";
    public static final String ERROR = "general.error";
    public static final String CREATED = "general.created";
    public static final String UPDATED = "general.updated";
    public static final String DELETED = "general.deleted";

    // Error messages
    public static final String ERROR_VALIDATION = "error.validation";
    public static final String ERROR_NOT_FOUND = "error.not_found";
    public static final String ERROR_BAD_REQUEST = "error.bad_request";
    public static final String ERROR_UNAUTHORIZED = "error.unauthorized";
    public static final String ERROR_FORBIDDEN = "error.forbidden";
    public static final String ERROR_INTERNAL = "error.internal";
    public static final String ERROR_BAD_CREDENTIALS = "error.bad_credentials";

    // Authentication & Authorization
    public static final String AUTH_LOGIN_SUCCESS = "auth.login.success";
    public static final String AUTH_LOGOUT_SUCCESS = "auth.logout.success";
    public static final String AUTH_REGISTER_SUCCESS = "auth.register.success";
    public static final String AUTH_TOKEN_INVALID = "auth.token.invalid";
    public static final String AUTH_TOKEN_EXPIRED = "auth.token.expired";

    // User messages
    public static final String USER_NOT_FOUND = "user.not_found";
    public static final String USER_ALREADY_EXISTS = "user.already_exists";
    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";

    private MessageCode() {
        // Private constructor to prevent instantiation
    }
}
