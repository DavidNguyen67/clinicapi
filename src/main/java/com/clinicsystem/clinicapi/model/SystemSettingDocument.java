package com.clinicsystem.clinicapi.model;

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

@Document(collection = "system_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Setting key is required")
    private String settingKey;

    private String settingValue;

    private String settingType;

    private String description;

    private Boolean isPublic = false;

    private String updatedByUserId; // Reference to User UUID

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
