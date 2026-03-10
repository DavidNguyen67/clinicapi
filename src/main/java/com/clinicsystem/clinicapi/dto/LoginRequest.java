package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.validation.Normalize;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.clinicsystem.clinicapi.validation.Normalize.NormalizeType.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = MessageCode.VALIDATION_EMAIL_REQUIRED)
    @Normalize({ TRIM, LOWERCASE })
    private String email;

    @NotBlank(message = MessageCode.VALIDATION_PASSWORD_REQUIRED)
    @Normalize(TRIM)
    private String password;
}
