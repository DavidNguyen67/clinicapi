package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.ConversationDocument;

@Repository
public interface ConversationMongoRepository extends MongoRepository<ConversationDocument, String> {

}
