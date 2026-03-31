package com.clinicsystem.clinicapi.dto;

import java.util.UUID;

import com.clinicsystem.clinicapi.constant.AuthEventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEventDto {
    private UUID userId;

    private AuthEventType eventType;

    private String email;

    private String fullName;
}
