package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@CompoundIndex(name = "idx_user_read", def = "{'userId': 1, 'isRead': 1}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDocument {

    @Id
    private String id;

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_USER_REQUIRED)
    private String userId; // Reference to User UUID

    @NotBlank(message = MessageCode.VALIDATION_TITLE_REQUIRED)
    private String title;

    @NotBlank(message = MessageCode.VALIDATION_MESSAGE_REQUIRED)
    private String message;

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_TYPE_REQUIRED)
    private NotificationType type;

    private String referenceType;

    private String referenceId;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum NotificationType {
        appointment, payment, reminder, promotion, system
    }

    // Helper method
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
