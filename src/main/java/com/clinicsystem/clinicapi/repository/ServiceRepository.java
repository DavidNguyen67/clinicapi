package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Faq;
import com.clinicsystem.clinicapi.model.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty")
    List<Service> findActiveForFirstPage(Pageable pageable);

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty " +
            "WHERE s.createdAt < :cur")
    List<Service> getAllServices(@Param("cur") LocalDateTime cur, Pageable pageable);

}
