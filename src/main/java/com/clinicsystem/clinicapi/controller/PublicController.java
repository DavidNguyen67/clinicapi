package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

        @GetMapping("/clinic-info")
        public ResponseEntity<ApiResponse<ClinicInfoDto>> getClinicInfo() {
                log.info("GET /api/v1/public/clinic-info - Get clinic information");

                ClinicInfoDto clinicInfo = ClinicInfoDto.builder()
                                .clinicName("ABC Medical Clinic")
                                .address("123 Main Street, District 1, Ho Chi Minh City, Vietnam")
                                .phone("+84 28 1234 5678")
                                .email("info@abcclinic.com")
                                .website("https://www.abcclinic.com")
                                .description("Leading healthcare provider with experienced doctors and modern facilities")
                                .logo("/assets/logo.png")
                                .openingHours("Monday - Friday: 8:00 AM - 8:00 PM | Saturday - Sunday: 8:00 AM - 5:00 PM")
                                .facilities(Arrays.asList(
                                                "Modern examination rooms",
                                                "Advanced diagnostic equipment",
                                                "24/7 emergency care",
                                                "Pharmacy",
                                                "Laboratory services",
                                                "Comfortable waiting areas"))
                                .totalDoctors(50)
                                .totalSpecialties(15)
                                .totalServices(100)
                                .build();

                return ResponseEntity.ok(ApiResponse.<ClinicInfoDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(clinicInfo)
                                .build());
        }
}
