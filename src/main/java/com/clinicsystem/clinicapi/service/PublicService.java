package com.clinicsystem.clinicapi.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.HomePublicDto;
import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicService {
        private final PatientService patientService;
        private final DoctorService doctorService;
        private final SpecialtyService specialtyService;
        private final SystemSettingService systemSettingService;

        @Transactional(readOnly = true, rollbackFor = Exception.class)
        public HomePublicDto getHomePublicData() {
                int totalPatients = patientService.getTotalPatients();
                int totalDoctors = doctorService.getTotalDoctors();
                int totalSpecialties = specialtyService.getTotalSpecialties();
                Map<SpecialtyType, Integer> doctorMap = specialtyService.getSpecialtiesGroupByType();

                return HomePublicDto.builder()
                                .totalPatients(totalPatients)
                                .totalDoctors(totalDoctors)
                                .totalSpecialties(totalSpecialties)
                                .doctorsMap(doctorMap)
                                .build();
        }

        @Transactional(readOnly = true, rollbackFor = Exception.class)
        public ClinicInfoDto getClinicInfo() {
                return ClinicInfoDto.builder()
                                .clinicName(systemSettingService.getSettingValue("clinic.name", "ABC Medical Clinic"))
                                .address(systemSettingService.getSettingValue("clinic.address",
                                                "123 Main Street, District 1, Ho Chi Minh City, Vietnam"))
                                .phone(systemSettingService.getSettingValue("clinic.phone", "+84 28 1234 5678"))
                                .email(systemSettingService.getSettingValue("clinic.email", "info@abcclinic.com"))
                                .pathToImage(systemSettingService.getSettingValue("clinic.logo", "/assets/logo.png"))
                                .openingHours(systemSettingService.getSettingValue("clinic.openingHours",
                                                "Monday - Friday: 8:00 AM - 8:00 PM | Saturday - Sunday: 8:00 AM - 5:00 PM"))
                                .averageRating(
                                                new BigDecimal(
                                                                systemSettingService.getSettingValue(
                                                                                "clinic.averageRating", "0.0")))
                                .build();
        }

}
