package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicInfoDto {
    private String clinicName;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String description;
    private String logo;
    private String openingHours;
    private List<String> facilities;
    private Integer totalDoctors;
    private Integer totalSpecialties;
    private Integer totalServices;
}
