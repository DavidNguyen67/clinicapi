package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.SystemSettingDocument;

import java.util.Optional;

@Repository
public interface SystemSettingMongoRepository extends MongoRepository<SystemSettingDocument, String> {
    Optional<SystemSettingDocument> findBySettingKey(String settingKey);
}
