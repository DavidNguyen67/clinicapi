package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
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
    @NotNull(message = MessageCode.VALIDATION_USER_REQUIRED)
    private String userId; // Reference to User UUID

    @NotBlank(message = MessageCode.VALIDATION_ACTIVITY_TYPE_REQUIRED)
    @Indexed
    private String activityType;

    private String description;

    private Map<String, Object> metadata;

    private String ipAddress;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;
}
