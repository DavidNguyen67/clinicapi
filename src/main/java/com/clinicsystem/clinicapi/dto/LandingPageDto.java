package com.clinicsystem.clinicapi.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandingPageDto {
    private int totalPatients;
    private int totalDoctors;
    private int totalSpecialties;
    private Map<SpecialtyType, Integer> doctorsMap;
    private BigDecimal averageRating;
}
