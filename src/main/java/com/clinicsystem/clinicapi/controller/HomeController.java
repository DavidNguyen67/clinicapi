package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.ClinicServiceDto;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.FaqDto;
import com.clinicsystem.clinicapi.dto.HealthNewsResponseDto;
import com.clinicsystem.clinicapi.dto.LandingPageDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.service.ClinicServiceService;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.FaqService;
import com.clinicsystem.clinicapi.service.HealthNewsService;
import com.clinicsystem.clinicapi.service.HomeService;
import com.clinicsystem.clinicapi.service.SpecialtyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
        private final HomeService homeService;
        private final DoctorService doctorService;
        private final ClinicServiceService clinicServiceService;
        private final FaqService faqService;
        private final SpecialtyService specialtyService;
        private final HealthNewsService healthNewsService;

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

        @GetMapping("/doctors")
        public ResponseEntity<ApiResponse<PageResponse<DoctorProfileDto>>> getAllDoctors(
                        @RequestParam(required = false) String lastId,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                PaginationDto paginationDto = PaginationDto.builder()
                                .lastId(lastId)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(Sort.Direction.fromString(sortDirection))
                                .build();

                PageResponse<DoctorProfileDto> doctors = doctorService.getAllDoctors(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<DoctorProfileDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(doctors)
                                .build());
        }

        @GetMapping("/services")
        public ResponseEntity<ApiResponse<PageResponse<ClinicServiceDto>>> getAllServices(
                        @RequestParam(required = false) String lastId,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                PaginationDto paginationDto = PaginationDto.builder()
                                .lastId(lastId)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(Sort.Direction.fromString(sortDirection))
                                .build();

                PageResponse<ClinicServiceDto> services = clinicServiceService.getAllServices(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<ClinicServiceDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(services)
                                .build());
        }

        @GetMapping("/faqs")
        public ResponseEntity<ApiResponse<PageResponse<FaqDto>>> getAllFaqs(
                        @RequestParam(required = false) String lastId,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                PaginationDto paginationDto = PaginationDto.builder()
                                .lastId(lastId)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(Sort.Direction.fromString(sortDirection))
                                .build();

                PageResponse<FaqDto> services = faqService.getAllFaqs(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<FaqDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(services)
                                .build());
        }

        @GetMapping("/specialties")
        public ResponseEntity<ApiResponse<PageResponse<SpecialtyDto>>> getAllSpecialties(
                        @RequestParam(required = false) String lastId,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {

                PaginationDto paginationDto = PaginationDto.builder()
                                .lastId(lastId)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(Sort.Direction.fromString(sortDirection))
                                .build();

                PageResponse<SpecialtyDto> services = specialtyService.getAllSpecialties(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<SpecialtyDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(services)
                                .build());
        }

        @GetMapping("/health-news")
        public ResponseEntity<ApiResponse<HealthNewsResponseDto>> getHealthNews(
                        @RequestParam(defaultValue = "vi") String lang,
                        @RequestParam(defaultValue = "vn") String country,
                        @RequestParam(defaultValue = "10") int max) {
                HealthNewsResponseDto news = healthNewsService.getTopHealthNews(lang, country, max);

                return ResponseEntity.ok(ApiResponse.<HealthNewsResponseDto>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(news)
                                .build());
        }

}
