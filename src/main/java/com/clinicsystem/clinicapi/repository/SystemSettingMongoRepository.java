package com.clinicsystem.clinicapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.SystemSettingDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingMongoRepository extends MongoRepository<SystemSettingDocument, String> {

    Optional<SystemSettingDocument> findBySettingKey(String settingKey);

    List<SystemSettingDocument> findBySettingType(String settingType);

    List<SystemSettingDocument> findByIsPublic(Boolean isPublic);

    @Query("{'isPublic': true, 'deletedAt': null}")
    List<SystemSettingDocument> findAllPublic();

    @Query("{'deletedAt': null}")
    List<SystemSettingDocument> findAllActive();

    @Query("{'settingKey': {$regex: ?0, $options: 'i'}, 'deletedAt': null}")
    List<SystemSettingDocument> searchByKey(String keyword);
}
