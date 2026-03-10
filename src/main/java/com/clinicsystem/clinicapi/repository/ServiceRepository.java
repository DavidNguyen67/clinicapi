package com.clinicsystem.clinicapi.repository;

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

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty " +
            "WHERE s.isActive = true ORDER BY s.createdAt DESC, s.id DESC")
    List<Service> findActiveForFirstPageDesc(Pageable pageable);

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty " +
            "WHERE s.isActive = true ORDER BY s.createdAt ASC, s.id ASC")
    List<Service> findActiveForFirstPageAsc(Pageable pageable);

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty " +
            "WHERE s.isActive = true AND s.createdAt < :cur ORDER BY s.createdAt DESC, s.id DESC")
    List<Service> findActiveAfterCursorDesc(@Param("cur") LocalDateTime cur, Pageable pageable);

    @Query("SELECT s FROM Service s LEFT JOIN FETCH s.specialty " +
            "WHERE s.isActive = true AND s.createdAt > :cur ORDER BY s.createdAt ASC, s.id ASC")
    List<Service> findActiveAfterCursorAsc(@Param("cur") LocalDateTime cur, Pageable pageable);
}
