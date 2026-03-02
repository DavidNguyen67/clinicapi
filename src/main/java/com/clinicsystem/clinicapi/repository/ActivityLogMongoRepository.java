package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.ActivityLogDocument;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogMongoRepository extends MongoRepository<ActivityLogDocument, String> {

    List<ActivityLogDocument> findByUserId(String userId);

    List<ActivityLogDocument> findByActivityType(String activityType);

    List<ActivityLogDocument> findByUserIdAndActivityType(String userId, String activityType);

    List<ActivityLogDocument> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<ActivityLogDocument> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate,
            LocalDateTime endDate);

    List<ActivityLogDocument> findByUserIdOrderByCreatedAtDesc(String userId);
}
