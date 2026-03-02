package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.AuditLogDocument;

@Repository
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {

}
