package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Staff;
import com.clinicsystem.clinicapi.model.Staff.StaffStatus;
import com.clinicsystem.clinicapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

    Optional<Staff> findByStaffCode(String staffCode);

    Optional<Staff> findByUser(User user);

    Optional<Staff> findByUserId(UUID userId);

    List<Staff> findByDepartment(String department);

    List<Staff> findByStatus(StaffStatus status);

    List<Staff> findByDepartmentAndStatus(String department, StaffStatus status);

    boolean existsByStaffCode(String staffCode);

    boolean existsByUserId(UUID userId);
}
