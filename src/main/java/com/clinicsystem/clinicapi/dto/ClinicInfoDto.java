package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicInfoDto {
    private String clinicName;
    private String address;
    private String phone;
    private String email;
    private String pathToImage;
    private String openingHours;
    private BigDecimal averageRating;
}
