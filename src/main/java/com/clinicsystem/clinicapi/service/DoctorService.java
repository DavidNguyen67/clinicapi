package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.FaqDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.model.Faq;
import com.clinicsystem.clinicapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public PageResponse<DoctorProfileDto> getAllDoctors(PaginationDto paginationDto) {
        int limit = paginationDto.getSize() + 1;
        PageRequest pageable = PageRequest.of(0, limit, paginationDto.getSortDirection(),
                paginationDto.getSortBy());

        List<Doctor> doctors;
        if (paginationDto.getLastId() == null || paginationDto.getLastId().isBlank()) {
            doctors = doctorRepository.findActiveForFirstPage(pageable);
        } else {
            UUID lastId = UUID.fromString(paginationDto.getLastId());
            Doctor lastDoctor = doctorRepository.findById(lastId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            MessageCode.DOCTOR_NOT_FOUND, "Cursor not found"));
            doctors = doctorRepository.getAllDoctors(lastDoctor.getCreatedAt(), pageable);
        }

        boolean hasMore = doctors.size() > paginationDto.getSize();
        if (hasMore) {
            doctors = doctors.subList(0, paginationDto.getSize());
        }

        List<DoctorProfileDto> records = doctors.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());

        return PageResponse.<DoctorProfileDto>builder()
                .records(records)
                .build();
    }

    @Transactional(readOnly = true)
    public DoctorProfileDto getDoctorById(UUID id) {
        log.info("Getting doctor by id: {}", id);
        Doctor doctor = doctorRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageCode.DOCTOR_NOT_FOUND,
                        "Doctor not found with id: " + id));

        if (doctor.getStatus() != Doctor.DoctorStatus.active) {
            throw new ResourceNotFoundException(
                    MessageCode.DOCTOR_NOT_FOUND,
                    "Doctor is not available");
        }

        return convertToPublicDto(doctor);
    }

    public DoctorProfileDto convertToPublicDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        SpecialtyDto specialtyDto = null;

        if (doctor.getSpecialty() != null) {
            specialtyDto = SpecialtyDto.builder()
                    .id(doctor.getSpecialty().getId())
                    .name(doctor.getSpecialty().getName())
                    .slug(doctor.getSpecialty().getSlug())
                    .description(doctor.getSpecialty().getDescription())
                    .image(doctor.getSpecialty().getImage())
                    .build();
        }

        return DoctorProfileDto.builder()
                .id(doctor.getId())
                .doctorCode(doctor.getDoctorCode())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .pathAvatar(doctor.getUser().getPathAvatar())
                .specialty(specialtyDto)
                .degree(doctor.getDegree())
                .experienceYears(doctor.getExperienceYears())
                .education(doctor.getEducation())
                .bio(doctor.getBio())
                .averageRating(doctor.getAverageRating())
                .totalReviews(doctor.getTotalReviews())
                .totalPatients(doctor.getTotalPatients())
                .isFeatured(doctor.getIsFeatured())
                .status(doctor.getStatus().name())
                .build();
    }

    @Transactional(readOnly = true)
    public int getTotalDoctors() {
        return doctorRepository.countByStatus(Doctor.DoctorStatus.active);
    }
}
