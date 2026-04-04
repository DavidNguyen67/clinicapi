package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.AuthEventType;
import com.clinicsystem.clinicapi.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEventDto {
    private User user;

    private AuthEventType eventType;

    private String email;

    private String fullName;

    private Integer expiryHours;

    private String resetToken;
}
