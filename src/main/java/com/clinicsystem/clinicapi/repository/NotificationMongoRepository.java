package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.NotificationDocument;

@Repository
public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {

}
