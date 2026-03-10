package com.clinicsystem.clinicapi.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.LandingPageDto;
import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
        private final PatientService patientService;
        private final DoctorService doctorService;
        private final SpecialtyService specialtyService;
        private final SystemSettingService systemSettingService;

        @Transactional(readOnly = true)
        public LandingPageDto getHomePublicData() {
                int totalPatients = patientService.getTotalPatients();
                int totalDoctors = doctorService.getTotalDoctors();
                int totalSpecialties = specialtyService.getTotalSpecialties();
                Map<SpecialtyType, Integer> doctorMap = specialtyService.getSpecialtiesGroupByType();
                BigDecimal averageRating = new BigDecimal(
                                systemSettingService.getSettingValue("clinic.averageRating", "0.0"));

                return LandingPageDto.builder()
                                .totalPatients(totalPatients)
                                .totalDoctors(totalDoctors)
                                .totalSpecialties(totalSpecialties)
                                .doctorsMap(doctorMap)
                                .averageRating(averageRating)
                                .build();
        }

        @Transactional(readOnly = true)
        public ClinicInfoDto getClinicInfo() {
                return ClinicInfoDto.builder()
                                .clinicName(systemSettingService.getSettingValue("clinic.name"))
                                .address(systemSettingService.getSettingValue("clinic.address"))
                                .phone(systemSettingService.getSettingValue("clinic.phone"))
                                .email(systemSettingService.getSettingValue("clinic.email"))
                                .pathToImage(systemSettingService.getSettingValue("clinic.logo"))
                                .openingHours(systemSettingService.getSettingValue("clinic.openingHours"))
                                .averageRating(
                                                new BigDecimal(
                                                                systemSettingService.getSettingValue(
                                                                                "clinic.averageRating")))
                                .description(systemSettingService.getSettingValue("clinic.description"))
                                .build();
        }

}
