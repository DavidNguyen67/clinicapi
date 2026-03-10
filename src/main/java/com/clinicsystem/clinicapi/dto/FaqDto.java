package com.clinicsystem.clinicapi.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDto {
    private UUID id;
    private String category;
    private String question;
    private Boolean isActive;
    private String answer;
}
