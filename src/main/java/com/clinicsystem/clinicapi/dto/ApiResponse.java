package com.clinicsystem.clinicapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String messageCode;
    private T data;
    private LocalDateTime timestamp;
    private Object errors;

    public static <T> ApiResponse<T> success(String messageCode, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .messageCode(messageCode)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String messageCode) {
        return success(messageCode, null);
    }

    public static <T> ApiResponse<T> error(String messageCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .messageCode(messageCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String messageCode, Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .messageCode(messageCode)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
