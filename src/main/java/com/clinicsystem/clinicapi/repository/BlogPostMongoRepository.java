package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.BlogPostDocument;

@Repository
public interface BlogPostMongoRepository extends MongoRepository<BlogPostDocument, String> {

}
