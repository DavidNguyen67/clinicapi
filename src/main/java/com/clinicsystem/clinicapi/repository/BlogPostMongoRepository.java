package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.BlogPostDocument;
import com.clinicsystem.clinicapi.model.BlogPostDocument.PostStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostMongoRepository extends MongoRepository<BlogPostDocument, String> {

    Optional<BlogPostDocument> findBySlug(String slug);

    List<BlogPostDocument> findByCategoryId(String categoryId);

    List<BlogPostDocument> findByAuthorId(String authorId);

    List<BlogPostDocument> findByStatus(PostStatus status);

    List<BlogPostDocument> findByCategoryIdAndStatus(String categoryId, PostStatus status);

    @Query("{'status': 'published', 'deletedAt': null}")
    List<BlogPostDocument> findAllPublished();

    @Query("{'categoryId': ?0, 'status': 'published', 'deletedAt': null}")
    List<BlogPostDocument> findPublishedByCategoryId(String categoryId);

    List<BlogPostDocument> findByStatusOrderByPublishedAtDesc(PostStatus status);

    List<BlogPostDocument> findByStatusOrderByViewCountDesc(PostStatus status);

    @Query("{'title': {$regex: ?0, $options: 'i'}, 'status': 'published', 'deletedAt': null}")
    List<BlogPostDocument> searchByTitle(String keyword);
}
