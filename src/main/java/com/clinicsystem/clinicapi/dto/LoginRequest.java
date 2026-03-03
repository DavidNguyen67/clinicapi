package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = MessageCode.VALIDATION_EMAIL_REQUIRED)
    private String email;

    @NotBlank(message = MessageCode.VALIDATION_PASSWORD_REQUIRED)
    private String password;
}
