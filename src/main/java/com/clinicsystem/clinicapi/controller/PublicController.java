package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.DoctorPublicDto;
import com.clinicsystem.clinicapi.dto.HomePublicDto;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.PublicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
        private final PublicService publicService;
        private final DoctorService doctorService;

        @GetMapping("/clinic-info")
        public ResponseEntity<ApiResponse<ClinicInfoDto>> getClinicInfo() {
                log.info("GET /api/v1/public/clinic-info - Get clinic information");

                ClinicInfoDto clinicInfo = publicService.getClinicInfo();

                return ResponseEntity.ok(ApiResponse.<ClinicInfoDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(clinicInfo)
                                .build());
        }

        @GetMapping("/landing-page")
        public ResponseEntity<ApiResponse<HomePublicDto>> getLandingPage() {
                HomePublicDto homePublicDto = publicService.getHomePublicData();

                return ResponseEntity.ok(ApiResponse.<HomePublicDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(homePublicDto)
                                .build());
        }

        @GetMapping("/top-doctors")
        public ResponseEntity<ApiResponse<List<DoctorPublicDto>>> getTopDoctors() {
                List<DoctorPublicDto> topDoctors = doctorService.getTopDoctors();
                return ResponseEntity.ok(ApiResponse.<List<DoctorPublicDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(topDoctors)
                                .build());
        }

}
