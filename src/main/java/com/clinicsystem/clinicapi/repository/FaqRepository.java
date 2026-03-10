package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Faq;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {

    @Query("""
            SELECT f FROM Faq f
            WHERE f.isActive = true
            """)
    Page<Faq> getAllActiveFaq(Pageable pageable);
}
