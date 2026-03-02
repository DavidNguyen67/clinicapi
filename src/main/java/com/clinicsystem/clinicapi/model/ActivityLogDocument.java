package com.clinicsystem.clinicapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDocument {

    @Id
    private String id;

    @Indexed
    @NotNull(message = "User is required")
    private String userId; // Reference to User UUID

    @NotBlank(message = "Activity type is required")
    @Indexed
    private String activityType;

    private String description;

    private Map<String, Object> metadata;

    private String ipAddress;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;
}
