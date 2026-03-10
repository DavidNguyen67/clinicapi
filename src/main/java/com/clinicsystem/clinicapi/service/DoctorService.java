package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.model.Doctor;
import com.clinicsystem.clinicapi.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageResponse<DoctorProfileDto> getAllDoctors(
            UUID specialtyId,
            Boolean isFeatured,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.info("Getting doctors - specialtyId: {}, isFeatured: {}, page: {}, size: {}",
                specialtyId, isFeatured, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage;

        // Filter by specialty and featured status
        if (specialtyId != null && isFeatured != null) {
            doctorPage = doctorRepository.findAll(pageable)
                    .map(doctor -> filterDoctors(doctor, specialtyId, isFeatured));
        } else if (specialtyId != null) {
            doctorPage = doctorRepository.findAll(pageable)
                    .map(doctor -> filterBySpecialty(doctor, specialtyId));
        } else if (isFeatured != null) {
            doctorPage = doctorRepository.findAll(pageable)
                    .map(doctor -> filterByFeatured(doctor, isFeatured));
        } else {
            doctorPage = doctorRepository.findAll(pageable);
        }

        List<DoctorProfileDto> doctorDtos = doctorPage.getContent().stream()
                .filter(doctor -> doctor != null && doctor.getStatus() == Doctor.DoctorStatus.active)
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());

        return PageResponse.<DoctorProfileDto>builder()
                .records(doctorDtos)
                .nextCursor(doctorPage.hasNext() ? String.valueOf(page + 1) : null)
                .hasMore(doctorPage.hasNext())
                .pageSize(doctorPage.getSize())
                .pageNumber(doctorPage.getNumber())
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
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

    private Doctor filterDoctors(Doctor doctor, UUID specialtyId, Boolean isFeatured) {
        if (doctor.getSpecialty() != null &&
                doctor.getSpecialty().getId().equals(specialtyId) &&
                doctor.getIsFeatured().equals(isFeatured)) {
            return doctor;
        }
        return null;
    }

    private Doctor filterBySpecialty(Doctor doctor, UUID specialtyId) {
        if (doctor.getSpecialty() != null &&
                doctor.getSpecialty().getId().equals(specialtyId)) {
            return doctor;
        }
        return null;
    }

    private Doctor filterByFeatured(Doctor doctor, Boolean isFeatured) {
        if (doctor.getIsFeatured().equals(isFeatured)) {
            return doctor;
        }
        return null;
    }

    private DoctorProfileDto convertToPublicDto(Doctor doctor) {
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

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<DoctorProfileDto> getTopDoctors() {
        List<Doctor> doctors = doctorRepository.getTopDoctors();
        return doctors.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public int getTotalDoctors() {
        return doctorRepository.countByStatus(Doctor.DoctorStatus.active);
    }
}
