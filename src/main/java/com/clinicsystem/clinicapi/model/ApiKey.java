package com.clinicsystem.clinicapi.model;

import java.security.Timestamp;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "api_keys", indexes = {
        @Index(name = "idx_status_expires", columnList = "status, expires_at")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApiKey extends SoftDeletableEntity {
    @Column(name = "key_hash")
    private Integer keyHash;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ApiKeyStatus status;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @Column(name = "revoked_at")
    private Timestamp revokedAt;

    @Column(name = "last_used_at")
    private Timestamp lastUsedAt;

    @Column(name = "rotated_from_id")
    private UUID rotatedFromId;

    public enum ApiKeyStatus {
        ACTIVE, INACTIVE, REVOKED, EXPIRED
    }

}
