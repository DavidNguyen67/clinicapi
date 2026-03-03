package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.clinicsystem.clinicapi.model.Role.RoleName;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_phone", columnList = "phone")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User extends SoftDeletableEntity {
    @NotBlank(message = MessageCode.VALIDATION_EMAIL_REQUIRED)
    @Email(message = MessageCode.VALIDATION_EMAIL_INVALID)
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @NotBlank(message = MessageCode.VALIDATION_DOB_REQUIRED)
    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;

    @NotBlank(message = MessageCode.VALIDATION_PASSWORD_REQUIRED)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotBlank(message = MessageCode.VALIDATION_PHONE_REQUIRED)
    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @NotBlank(message = MessageCode.VALIDATION_FULLNAME_REQUIRED)
    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleName role = RoleName.ROLE_PATIENT;

    @Column(length = 500)
    private String pathAvatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender = Gender.OTHER;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    private Boolean phoneVerified = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public enum UserStatus {
        ACTIVE, INACTIVE, BANNED
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
