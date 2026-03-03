package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch(password = "newPassword", confirmPassword = "confirmPassword")
public class ChangePasswordRequest {

    @NotBlank(message = MessageCode.VALIDATION_CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;

    @NotBlank(message = MessageCode.VALIDATION_NEW_PASSWORD_REQUIRED)
    @Size(min = 6, message = MessageCode.VALIDATION_PASSWORD_MIN_SIZE)
    private String newPassword;

    @NotBlank(message = MessageCode.VALIDATION_CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;
}
