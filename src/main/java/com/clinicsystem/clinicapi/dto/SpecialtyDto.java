package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyDto {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String image;
    private Boolean isActive;
    private List<DoctorProfileDto> doctors;

}
