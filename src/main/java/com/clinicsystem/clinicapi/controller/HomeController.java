package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.LandingPageDto;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.HomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
        private final HomeService homeService;
        private final DoctorService doctorService;

        @GetMapping("/clinic-info")
        public ResponseEntity<ApiResponse<ClinicInfoDto>> getClinicInfo() {
                log.info("GET /api/v1/home/clinic-info - Get clinic information");

                ClinicInfoDto clinicInfo = homeService.getClinicInfo();

                return ResponseEntity.ok(ApiResponse.<ClinicInfoDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(clinicInfo)
                                .build());
        }

        @GetMapping("/landing-page")
        public ResponseEntity<ApiResponse<LandingPageDto>> getLandingPage() {
                LandingPageDto landingPageDto = homeService.getHomePublicData();

                return ResponseEntity.ok(ApiResponse.<LandingPageDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(landingPageDto)
                                .build());
        }

        @GetMapping("/top-doctors")
        public ResponseEntity<ApiResponse<List<DoctorProfileDto>>> getTopDoctors() {
                List<DoctorProfileDto> topDoctors = doctorService.getTopDoctors();
                return ResponseEntity.ok(ApiResponse.<List<DoctorProfileDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(topDoctors)
                                .build());
        }

}
