package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.dto.SpecialtyTypeCountDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.model.Specialty;
import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;
import com.clinicsystem.clinicapi.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        @Transactional(readOnly = true)
        public PageResponse<SpecialtyDto> getAllActiveSpecialties(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {
                log.info("Getting all active specialties - page: {}, size: {}", page, size);

                Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<Specialty> specialtyPage = specialtyRepository.findAll(pageable);

                List<SpecialtyDto> specialtyDtos = specialtyPage.getContent().stream()
                                .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return PageResponse.<SpecialtyDto>builder()
                                .records(specialtyDtos)
                                .nextCursor(specialtyPage.hasNext() ? String.valueOf(page + 1) : null)
                                .hasMore(specialtyPage.hasNext())
                                .pageSize(specialtyPage.getSize())
                                .pageNumber(specialtyPage.getNumber())
                                .totalPages(specialtyPage.getTotalPages())
                                .totalElements(specialtyPage.getTotalElements())
                                .build();
        }

        @Transactional(readOnly = true)
        public SpecialtyDto getSpecialtyById(UUID id) {
                log.info("Getting specialty by id: {}", id);
                Specialty specialty = specialtyRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                MessageCode.SPECIALTY_NOT_FOUND,
                                                "Specialty not found with id: " + id));

                return convertToDto(specialty);
        }

        private SpecialtyDto convertToDto(Specialty specialty) {
                return SpecialtyDto.builder()
                                .id(specialty.getId())
                                .name(specialty.getName())
                                .slug(specialty.getSlug())
                                .description(specialty.getDescription())
                                .image(specialty.getImage())
                                .displayOrder(specialty.getDisplayOrder())
                                .isActive(specialty.getIsActive())
                                .createdAt(specialty.getCreatedAt())
                                .updatedAt(specialty.getUpdatedAt())
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
