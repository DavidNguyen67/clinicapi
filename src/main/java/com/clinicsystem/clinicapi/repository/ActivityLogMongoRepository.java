package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.ActivityLogDocument;

@Repository
public interface ActivityLogMongoRepository extends MongoRepository<ActivityLogDocument, String> {

}
