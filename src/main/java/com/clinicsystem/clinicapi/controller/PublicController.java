package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ClinicInfoDto;
import com.clinicsystem.clinicapi.dto.DoctorPublicDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.ServicePublicDto;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.service.DoctorService;
import com.clinicsystem.clinicapi.service.ServiceManagementService;
import com.clinicsystem.clinicapi.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final DoctorService doctorService;
    private final ServiceManagementService serviceManagementService;
    private final SpecialtyService specialtyService;

    @GetMapping("/specialties")
    public ResponseEntity<ApiResponse<PageResponse<SpecialtyDto>>> getAllSpecialties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/v1/public/specialties - page: {}, size: {}", page, size);

        PageResponse<SpecialtyDto> specialties = specialtyService.getAllActiveSpecialties(
                page, size, sortBy, sortDirection);

        return ResponseEntity.ok(ApiResponse.<PageResponse<SpecialtyDto>>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(specialties)
                .build());
    }

    @GetMapping("/specialties/{id}")
    public ResponseEntity<ApiResponse<SpecialtyDto>> getSpecialtyById(@PathVariable UUID id) {
        log.info("GET /api/v1/public/specialties/{} - Get specialty by id", id);

        SpecialtyDto specialty = specialtyService.getSpecialtyById(id);

        return ResponseEntity.ok(ApiResponse.<SpecialtyDto>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(specialty)
                .build());
    }

    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<PageResponse<DoctorPublicDto>>> getAllDoctors(
            @RequestParam(required = false) UUID specialtyId,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("GET /api/v1/public/doctors - specialtyId: {}, isFeatured: {}, page: {}, size: {}",
                specialtyId, isFeatured, page, size);

        PageResponse<DoctorPublicDto> doctors = doctorService.getAllDoctors(
                specialtyId, isFeatured, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(ApiResponse.<PageResponse<DoctorPublicDto>>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(doctors)
                .build());
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorPublicDto>> getDoctorById(@PathVariable UUID id) {
        log.info("GET /api/v1/public/doctors/{} - Get doctor by id", id);

        DoctorPublicDto doctor = doctorService.getDoctorById(id);

        return ResponseEntity.ok(ApiResponse.<DoctorPublicDto>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(doctor)
                .build());
    }

    @GetMapping("/services")
    public ResponseEntity<ApiResponse<PageResponse<ServicePublicDto>>> getAllServices(
            @RequestParam(required = false) UUID specialtyId,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("GET /api/v1/public/services - specialtyId: {}, isFeatured: {}, page: {}, size: {}",
                specialtyId, isFeatured, page, size);

        PageResponse<ServicePublicDto> services = serviceManagementService.getAllServices(
                specialtyId, isFeatured, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(ApiResponse.<PageResponse<ServicePublicDto>>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(services)
                .build());
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServicePublicDto>> getServiceById(@PathVariable UUID id) {
        log.info("GET /api/v1/public/services/{} - Get service by id", id);

        ServicePublicDto service = serviceManagementService.getServiceById(id);

        return ResponseEntity.ok(ApiResponse.<ServicePublicDto>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(service)
                .build());
    }

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
