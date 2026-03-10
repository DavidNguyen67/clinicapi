package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.ClinicServiceDto;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.FaqDto;
import com.clinicsystem.clinicapi.dto.LandingPageDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.service.ClinicServiceService;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.FaqService;
import com.clinicsystem.clinicapi.service.HomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.repository.query.Param;
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
        private final ClinicServiceService clinicServiceService;
        private final FaqService faqService;

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

        @GetMapping("/services")
        public ResponseEntity<ApiResponse<PageResponse<ClinicServiceDto>>> getAllServices(
                        @RequestParam(defaultValue = "false") Boolean isFeatured,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {
                PaginationDto paginationDto = PaginationDto.builder()
                                .isFeatured(isFeatured)
                                .page(page)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(sortDirection)
                                .build();

                PageResponse<ClinicServiceDto> services = clinicServiceService.getAllServices(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<ClinicServiceDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(services)
                                .build());
        }

        @GetMapping("/faq")
        public ResponseEntity<ApiResponse<PageResponse<FaqDto>>> getAllFaq(
                        @RequestParam(defaultValue = "false") Boolean isFeatured,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDirection) {
                PaginationDto paginationDto = PaginationDto.builder()
                                .isFeatured(isFeatured)
                                .page(page)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(sortDirection)
                                .build();

                PageResponse<FaqDto> services = faqService.getAllFaq(paginationDto);
                return ResponseEntity.ok(ApiResponse.<PageResponse<FaqDto>>builder()
                                .success(true)
                                .messageCode(MessageCode.GENERAL_SUCCESS)
                                .data(services)
                                .build());
        }
}
