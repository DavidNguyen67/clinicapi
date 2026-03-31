package com.clinicsystem.clinicapi.exception;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

        @ExceptionHandler(InvalidApiKeyException.class)
        public ResponseEntity<ApiResponse<Object>> handleInvalidApiKeyException(
                        InvalidApiKeyException ex, WebRequest request) {
                log.error("Invalid API key: {}", ex.getMessage());
                String messageCode = ex.getMessage().contains("missing")
                                ? MessageCode.API_KEY_MISSING
                                : MessageCode.API_KEY_INVALID;
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error(messageCode, buildClientError(ex)));
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
                        ResourceNotFoundException ex, WebRequest request) {
                log.error("Resource not found: {}", ex.getMessage());
                String messageCode = ex.getMessageCode() != null ? ex.getMessageCode() : MessageCode.ERROR_NOT_FOUND;

                Map<String, Object> errors = buildClientError(ex);
                if (ex.getResourceName() != null) {
                        errors.put("resource", ex.getResourceName());
                        errors.put("field", ex.getFieldName());
                        errors.put("value", ex.getFieldValue());
                }

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error(messageCode, errors));
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
                        BadRequestException ex, WebRequest request) {
                log.error("Bad request: {}", ex.getMessage());
                String messageCode = ex.getMessageCode() != null ? ex.getMessageCode() : MessageCode.ERROR_BAD_REQUEST;
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(messageCode, buildClientError(ex)));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {
                Map<String, String> fieldErrors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String field = ((FieldError) error).getField();
                        fieldErrors.put(field, error.getDefaultMessage());
                });

                log.error("Validation failed: {}", fieldErrors);

                Map<String, Object> errors = buildClientError(ex);
                errors.put("fields", fieldErrors);
                errors.put("totalErrors", fieldErrors.size());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(MessageCode.ERROR_VALIDATION, errors));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
                        IllegalArgumentException ex, WebRequest request) {
                log.error("Illegal argument: {}", ex.getMessage());
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(MessageCode.ERROR_BAD_REQUEST, buildClientError(ex)));
        }

        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(
                        UsernameNotFoundException ex, WebRequest request) {
                log.error("Username not found: {}", ex.getMessage());
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error(MessageCode.USER_NOT_FOUND, buildClientError(ex)));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
                        BadCredentialsException ex, WebRequest request) {
                log.error("Bad credentials: {}", ex.getMessage());
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error(MessageCode.ERROR_BAD_CREDENTIALS, buildClientError(ex)));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
                        AccessDeniedException ex, WebRequest request) {
                log.error("Access denied: {}", ex.getMessage());
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.error(MessageCode.ERROR_FORBIDDEN, buildClientError(ex)));
        }

        @ExceptionHandler(NullPointerException.class)
        public ResponseEntity<ApiResponse<Object>> handleNullPointerException(
                        NullPointerException ex, WebRequest request) {
                log.error("Null pointer exception occurred", ex);
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(MessageCode.ERROR_INTERNAL, buildClientError(ex)));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleGlobalException(
                        Exception ex, WebRequest request) {
                log.error("Unexpected error occurred", ex);
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(MessageCode.ERROR_INTERNAL, buildClientError(ex)));
        }

        private Map<String, Object> buildClientError(Exception ex) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("message", ex.getMessage());
                return errors;
        }
}