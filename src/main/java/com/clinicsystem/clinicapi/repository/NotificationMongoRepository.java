package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.NotificationDocument;
import com.clinicsystem.clinicapi.model.NotificationDocument.NotificationType;

import java.util.List;

@Repository
public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {

    List<NotificationDocument> findByUserId(String userId);

    List<NotificationDocument> findByUserIdAndIsRead(String userId, Boolean isRead);

    List<NotificationDocument> findByUserIdOrderByCreatedAtDesc(String userId);

    List<NotificationDocument> findByType(NotificationType type);

    List<NotificationDocument> findByUserIdAndType(String userId, NotificationType type);

    @Query("{'userId': ?0, 'deletedAt': null}")
    List<NotificationDocument> findActiveByUserId(String userId);

    @Query("{'userId': ?0, 'isRead': false, 'deletedAt': null}")
    List<NotificationDocument> findUnreadByUserId(String userId);

    Long countByUserIdAndIsRead(String userId, Boolean isRead);
}
