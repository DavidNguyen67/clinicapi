package com.clinicsystem.clinicapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@SQLDelete(sql = "UPDATE #{#entityName} SET deleted_at = NOW(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Soft delete this entity
     */
    public void softDelete(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }

    /**
     * Restore soft deleted entity
     */
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.isDeleted = false;
    }

    /**
     * Check if entity is soft deleted
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
}
