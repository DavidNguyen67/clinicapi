package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Service;
import com.clinicsystem.clinicapi.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    Optional<Service> findBySlug(String slug);

    List<Service> findBySpecialty(Specialty specialty);

    List<Service> findBySpecialtyId(UUID specialtyId);

    List<Service> findByIsActiveTrueAndIsFeaturedTrue();

    List<Service> findByIsActiveTrue();

    List<Service> findBySpecialtyIdAndIsActiveTrue(UUID specialtyId);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}
