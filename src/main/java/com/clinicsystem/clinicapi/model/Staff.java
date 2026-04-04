package com.clinicsystem.clinicapi.model;

import java.time.LocalDateTime;

import com.clinicsystem.clinicapi.constant.MessageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "staff", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_staff_code", columnList = "staff_code"),
        @Index(name = "idx_department", columnList = "department"),
        @Index(name = "idx_status", columnList = "status")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Staff extends SoftDeletableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false, foreignKey = @ForeignKey(name = "fk_staff_user"))
    @NotNull(message = MessageCode.VALIDATION_USER_REQUIRED)
    private User user;

    @NotBlank(message = MessageCode.VALIDATION_STAFF_CODE_REQUIRED)
    @Column(name = "staff_code", unique = true, nullable = false, length = 20)
    private String staffCode;

    @Column(length = 100)
    private String position;

    @Column(length = 100)
    private String department;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StaffStatus status = StaffStatus.active;

    public enum StaffStatus {
        active, on_leave, resigned
    }
}
