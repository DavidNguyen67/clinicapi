package com.clinicsystem.clinicapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicsystem.clinicapi.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

}