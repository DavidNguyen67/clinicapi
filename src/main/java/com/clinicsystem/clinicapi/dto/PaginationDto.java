package com.clinicsystem.clinicapi.dto;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
    private String lastId;
    private int size;
    private String sortBy;
    private Sort.Direction sortDirection;
}
