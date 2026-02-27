package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private List<SearchCriteria> criteriaList;
    private String sortBy;
    private String sortDirection; // ASC, DESC
    private Integer page;
    private Integer size;
    private String cursor; // For cursor-based pagination
}
