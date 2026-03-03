package com.clinicsystem.clinicapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.clinicsystem.clinicapi.repository")
@EnableMongoAuditing
public class MongoConfig {
}
