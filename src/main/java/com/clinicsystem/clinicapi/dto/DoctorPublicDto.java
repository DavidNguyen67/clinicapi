package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPublicDto {
    private UUID id;
    private String doctorCode;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private SpecialtyDto specialty;
    private String degree;
    private Integer experienceYears;
    private String education;
    private String bio;
    private BigDecimal consultationFee;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer totalPatients;
    private Boolean isFeatured;
    private String status;
}
