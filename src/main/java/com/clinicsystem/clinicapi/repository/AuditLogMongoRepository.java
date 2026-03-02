package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.AuditLogDocument;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {

    List<AuditLogDocument> findByUserId(String userId);

    List<AuditLogDocument> findByTableName(String tableName);

    List<AuditLogDocument> findByAction(String action);

    List<AuditLogDocument> findByTableNameAndRecordId(String tableName, String recordId);

    List<AuditLogDocument> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<AuditLogDocument> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate,
            LocalDateTime endDate);

    List<AuditLogDocument> findByUserIdOrderByCreatedAtDesc(String userId);
}
