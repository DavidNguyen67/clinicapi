package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.MessageDocument;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageDocument, String> {

    List<MessageDocument> findByConversationId(String conversationId);

    List<MessageDocument> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    List<MessageDocument> findByConversationIdOrderByCreatedAtDesc(String conversationId);

    List<MessageDocument> findBySenderId(String senderId);

    List<MessageDocument> findByConversationIdAndIsRead(String conversationId, Boolean isRead);

    @Query("{'conversationId': ?0, 'deletedAt': null}")
    List<MessageDocument> findActiveByConversationId(String conversationId);

    @Query("{'conversationId': ?0, 'isRead': false, 'deletedAt': null}")
    List<MessageDocument> findUnreadByConversationId(String conversationId);

    Long countByConversationIdAndIsRead(String conversationId, Boolean isRead);

    List<MessageDocument> findByConversationIdAndCreatedAtBetween(
            String conversationId, LocalDateTime startDate, LocalDateTime endDate);
}
