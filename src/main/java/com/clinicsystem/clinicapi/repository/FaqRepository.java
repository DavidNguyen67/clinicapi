package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {

    List<Faq> findByCategory(String category);

    List<Faq> findByIsActiveTrue();

    List<Faq> findByCategoryAndIsActiveTrue(String category);

    @Query("SELECT f FROM Faq f WHERE f.isActive = true ORDER BY f.displayOrder ASC, f.createdAt DESC")
    List<Faq> findActiveFaqsOrderByDisplayOrder();

    @Query("SELECT f FROM Faq f WHERE f.category = :category AND f.isActive = true " +
            "ORDER BY f.displayOrder ASC, f.createdAt DESC")
    List<Faq> findActiveFaqsByCategoryOrderByDisplayOrder(@Param("category") String category);

    @Query("SELECT DISTINCT f.category FROM Faq f WHERE f.isActive = true ORDER BY f.category")
    List<String> findDistinctCategories();
}
