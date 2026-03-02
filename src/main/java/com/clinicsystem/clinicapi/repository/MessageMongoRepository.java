package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.MessageDocument;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageDocument, String> {

}
