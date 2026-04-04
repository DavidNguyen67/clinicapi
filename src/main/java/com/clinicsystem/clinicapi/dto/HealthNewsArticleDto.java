package com.clinicsystem.clinicapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthNewsArticleDto {
    private String title;
    private String description;
    private String content;
    private String url;
    private String image;
    private String publishedAt;
    private String lang;
    private HealthNewsSourceDto source;
}
