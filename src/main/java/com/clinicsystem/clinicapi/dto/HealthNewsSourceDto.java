package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthNewsSourceDto {
    private String id;
    private String name;
    private String url;
}