package com.clinicsystem.clinicapi.dto;

import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;

public interface SpecialtyTypeCountDto {
    SpecialtyType getSpecialtyType();

    Integer getCount();
}
