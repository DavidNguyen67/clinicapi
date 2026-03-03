package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "blog_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategoryDocument {

    @Id
    private String id;

    @NotBlank(message = MessageCode.VALIDATION_NAME_REQUIRED)
    private String name;

    @Indexed(unique = true)
    @NotBlank(message = MessageCode.VALIDATION_SLUG_REQUIRED)
    private String slug;

    private String description;

    @Indexed
    private Boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
