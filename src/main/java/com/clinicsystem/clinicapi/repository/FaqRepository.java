package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Doctor.DoctorStatus;
import com.clinicsystem.clinicapi.model.Faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {

        @Query("SELECT f FROM Faq f ")
        List<Faq> findActiveForFirstPage(Pageable pageable);

        @Query("SELECT f FROM Faq f " +
                        "WHERE f.createdAt < :cur ORDER BY f.createdAt DESC")
        List<Faq> getAllFaqs(@Param("cur") LocalDateTime cur, Pageable pageable);
}
