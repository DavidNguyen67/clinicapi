package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
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

@Document(collection = "conversations")
@CompoundIndex(name = "unique_conversation", def = "{'patientId': 1, 'doctorId': 1}", unique = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDocument {

    @Id
    private String id;

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_PATIENT_REQUIRED)
    private String patientId; // Reference to Patient UUID

    @Indexed
    @NotNull(message = MessageCode.VALIDATION_DOCTOR_REQUIRED)
    private String doctorId; // Reference to Doctor UUID

    @Indexed
    private ConversationStatus status = ConversationStatus.active;

    @Indexed
    private LocalDateTime lastMessageAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum ConversationStatus {
        active, closed
    }

    // Helper method
    public void updateLastMessageTime() {
        this.lastMessageAt = LocalDateTime.now();
    }
}
