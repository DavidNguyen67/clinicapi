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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDocument {

    @Id
    private String id;

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_CONVERSATION_REQUIRED)
    private String conversationId; // Reference to Conversation

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_SENDER_REQUIRED)
    private String senderId; // Reference to User UUID

    @NotBlank(message = MessageCode.VALIDATION_MESSAGE_REQUIRED)
    private String message;

    private List<String> attachments;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // Helper method
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
