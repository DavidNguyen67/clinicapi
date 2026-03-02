package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.ConversationDocument;
import com.clinicsystem.clinicapi.model.ConversationDocument.ConversationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationMongoRepository extends MongoRepository<ConversationDocument, String> {

    List<ConversationDocument> findByPatientId(String patientId);

    List<ConversationDocument> findByDoctorId(String doctorId);

    Optional<ConversationDocument> findByPatientIdAndDoctorId(String patientId, String doctorId);

    List<ConversationDocument> findByStatus(ConversationStatus status);

    List<ConversationDocument> findByPatientIdAndStatus(String patientId, ConversationStatus status);

    List<ConversationDocument> findByDoctorIdAndStatus(String doctorId, ConversationStatus status);

    @Query("{'patientId': ?0, 'deletedAt': null}")
    List<ConversationDocument> findActiveByPatientId(String patientId);

    @Query("{'doctorId': ?0, 'deletedAt': null}")
    List<ConversationDocument> findActiveByDoctorId(String doctorId);

    List<ConversationDocument> findByPatientIdOrderByLastMessageAtDesc(String patientId);

    List<ConversationDocument> findByDoctorIdOrderByLastMessageAtDesc(String doctorId);
}
