package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    Optional<Specialty> findBySlug(String slug);

    List<Specialty> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<Specialty> findAllByOrderByDisplayOrderAsc();

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}
