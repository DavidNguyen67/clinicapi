package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.validation.Normalize;
import com.clinicsystem.clinicapi.validation.UniqueEmail;
import com.clinicsystem.clinicapi.validation.UniquePhone;
import com.clinicsystem.clinicapi.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.clinicsystem.clinicapi.validation.Normalize.NormalizeType.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = MessageCode.VALIDATION_EMAIL_REQUIRED)
    @Email(message = MessageCode.VALIDATION_EMAIL_INVALID)
    @UniqueEmail
    @Normalize({ TRIM, LOWERCASE })
    private String email;

    @NotBlank(message = MessageCode.VALIDATION_PASSWORD_REQUIRED)
    @Size(min = 6, message = MessageCode.VALIDATION_PASSWORD_MIN_SIZE)
    @Normalize(TRIM)
    private String password;

    @NotBlank(message = MessageCode.VALIDATION_PHONE_REQUIRED)
    @ValidPhoneNumber
    @UniquePhone
    @Normalize({ TRIM, REMOVE_ALL_WHITESPACE })
    private String phone;

    @NotBlank(message = MessageCode.VALIDATION_FULLNAME_REQUIRED)
    @Size(min = 2, max = 255, message = MessageCode.VALIDATION_FULLNAME_SIZE)
    @Normalize({ TRIM, COLLAPSE_WHITESPACE })
    private String fullName;

    @NotBlank(message = MessageCode.VALIDATION_DOB_REQUIRED)
    @Normalize(TRIM)
    private String dateOfBirth;

    @Normalize(TRIM)
    private String pathAvatar;
}
