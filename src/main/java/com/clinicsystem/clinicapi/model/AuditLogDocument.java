package com.clinicsystem.clinicapi.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDocument {

    @Id
    private String id;

    @Indexed
    private String userId; // Reference to User UUID

    @NotBlank(message = "Action is required")
    private String action;

    @Indexed
    private String tableName;

    private String recordId;

    private Map<String, Object> oldValues;

    private Map<String, Object> newValues;

    private String ipAddress;

    private String userAgent;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;
}
