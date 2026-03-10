package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.DoctorProfileDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.dto.SpecialtyTypeCountDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.model.Specialty;
import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;
import com.clinicsystem.clinicapi.repository.SpecialtyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialtyService {

        private final SpecialtyRepository specialtyRepository;
        private final DoctorService doctorService;

        @Transactional(readOnly = true)
        public PageResponse<SpecialtyDto> getAllSpecialties(PaginationDto paginationDto) {
                int limit = paginationDto.getSize() + 1;
                PageRequest pageable = PageRequest.of(0, limit, paginationDto.getSortDirection(),
                                paginationDto.getSortBy());

                List<Specialty> specialties;
                if (paginationDto.getLastId() == null || paginationDto.getLastId().isBlank()) {
                        specialties = specialtyRepository.findActiveForFirstPage(pageable);
                } else {
                        UUID lastId = UUID.fromString(paginationDto.getLastId());
                        Specialty lastSpecialty = specialtyRepository.findById(lastId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        MessageCode.SPECIALTY_NOT_FOUND, "Cursor not found"));
                        specialties = specialtyRepository.getAllSpecialties(lastSpecialty.getCreatedAt(), pageable);
                }

                boolean hasMore = specialties.size() > paginationDto.getSize();
                if (hasMore) {
                        specialties = specialties.subList(0, paginationDto.getSize());
                }

                List<SpecialtyDto> records = specialties.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return PageResponse.<SpecialtyDto>builder()
                                .records(records)
                                .build();
        }

        private SpecialtyDto convertToDto(com.clinicsystem.clinicapi.model.Specialty specialty) {
                List<DoctorProfileDto> doctorDtos = specialty.getDoctors().stream()
                                .map(doctor -> doctorService.convertToPublicDto(doctor))
                                .collect(Collectors.toList());

                return SpecialtyDto.builder()
                                .id(specialty.getId())
                                .name(specialty.getName())
                                .description(specialty.getDescription())
                                .isActive(specialty.getIsActive())
                                .doctors(doctorDtos)
                                .build();
        }

        @Transactional(readOnly = true)
        public int getTotalSpecialties() {
                int count = specialtyRepository.countByIsActive(Boolean.TRUE);
                return count > Integer.MAX_VALUE ? Integer.MAX_VALUE : count;
        }

        @Transactional(readOnly = true)
        public Map<SpecialtyType, Integer> getSpecialtiesGroupByType() {
                List<SpecialtyTypeCountDto> results = specialtyRepository.countGroupByType();

                Map<SpecialtyType, Integer> map = new EnumMap<>(SpecialtyType.class);

                results.forEach(dto -> {
                        map.put(dto.getSpecialtyType(), dto.getCount());
                });

                return map;
        }
}
