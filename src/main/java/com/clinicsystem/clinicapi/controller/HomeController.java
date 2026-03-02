package com.clinicsystem.clinicapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.DoctorPublicDto;
import com.clinicsystem.clinicapi.dto.HomePublicDto;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.HomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;
    private final DoctorService doctorService;

    @GetMapping("/landing-page")
    public ResponseEntity<ApiResponse<HomePublicDto>> getLandingPage() {
        HomePublicDto homePublicDto = homeService.getHomePublicData();

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
