package com.clinicsystem.clinicapi.config;

import com.clinicsystem.clinicapi.model.Role;
import com.clinicsystem.clinicapi.repository.RoleRepository;
import com.clinicsystem.clinicapi.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final SystemSettingService systemSettingService;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Initialize roles
            for (Role.RoleName roleName : Role.RoleName.values()) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    log.info("Created role: {}", roleName);
                }
            }

            // Initialize clinic settings
            initializeClinicSettings();

            log.info("Data initialization completed");
        };
    }

    private void initializeClinicSettings() {
        // Check if clinic settings already exist
        if (!systemSettingService.settingExists("clinic.name")) {
            systemSettingService.saveSetting("clinic.name", "ABC Medical Clinic",
                    "Clinic name", true);
            log.info("Initialized clinic.name setting");
        }

        if (!systemSettingService.settingExists("clinic.address")) {
            systemSettingService.saveSetting("clinic.address",
                    "123 Main Street, District 1, Ho Chi Minh City, Vietnam",
                    "Clinic address", true);
            log.info("Initialized clinic.address setting");
        }

        if (!systemSettingService.settingExists("clinic.phone")) {
            systemSettingService.saveSetting("clinic.phone", "+84 28 1234 5678",
                    "Clinic phone number", true);
            log.info("Initialized clinic.phone setting");
        }

        if (!systemSettingService.settingExists("clinic.email")) {
            systemSettingService.saveSetting("clinic.email", "info@abcclinic.com",
                    "Clinic email", true);
            log.info("Initialized clinic.email setting");
        }

        if (!systemSettingService.settingExists("clinic.logo")) {
            systemSettingService.saveSetting("clinic.logo", "/assets/logo.png",
                    "Clinic logo path", true);
            log.info("Initialized clinic.logo setting");
        }

        if (!systemSettingService.settingExists("clinic.openingHours")) {
            systemSettingService.saveSetting("clinic.openingHours",
                    "Monday - Friday: 8:00 AM - 8:00 PM | Saturday - Sunday: 8:00 AM - 5:00 PM",
                    "Clinic opening hours", true);
            log.info("Initialized clinic.openingHours setting");
        }
        if (!systemSettingService.settingExists("clinic.averageRating")) {
            systemSettingService.saveSetting("clinic.averageRating", "4.5",
                    "Clinic average rating", true);
            log.info("Initialized clinic.averageRating setting");
        }

        if (!systemSettingService.settingExists("clinic.description")) {
            systemSettingService.saveSetting("clinic.description",
                    "Phòng khám Đa khoa MedCare - Chăm sóc sức khỏe toàn diện",
                    "Clinic description", true);
            log.info("Initialized clinic.description setting");
        }
    }
}
