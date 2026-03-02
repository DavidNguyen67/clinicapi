package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.BlogCategoryDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogCategoryMongoRepository extends MongoRepository<BlogCategoryDocument, String> {

    Optional<BlogCategoryDocument> findBySlug(String slug);

    List<BlogCategoryDocument> findByIsActive(Boolean isActive);

    @Query("{'isActive': true, 'deletedAt': null}")
    List<BlogCategoryDocument> findAllActive();

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'deletedAt': null}")
    List<BlogCategoryDocument> searchByName(String keyword);
}
