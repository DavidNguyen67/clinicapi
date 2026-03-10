package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.dto.ClinicServiceDto;
import com.clinicsystem.clinicapi.dto.SpecialtyDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.repository.ServiceRepository;
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
public class ClinicServiceService {

        private final ServiceRepository serviceRepository;

        @Transactional(readOnly = true)
        public PageResponse<ClinicServiceDto> getAllServices(PaginationDto paginationDto) {
                int limit = paginationDto.getSize() + 1;
                PageRequest pageable = PageRequest.of(0, limit);
                boolean desc = "desc".equalsIgnoreCase(paginationDto.getSortDirection());

                List<com.clinicsystem.clinicapi.model.Service> services;
                if (paginationDto.getLastId() == null || paginationDto.getLastId().isBlank()) {
                        services = desc
                                        ? serviceRepository.findActiveForFirstPageDesc(pageable)
                                        : serviceRepository.findActiveForFirstPageAsc(pageable);
                } else {
                        UUID lastId = UUID.fromString(paginationDto.getLastId());
                        com.clinicsystem.clinicapi.model.Service lastService = serviceRepository.findById(lastId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        MessageCode.SERVICE_NOT_FOUND, "Cursor not found"));
                        services = desc
                                        ? serviceRepository.findActiveAfterCursorDesc(lastService.getCreatedAt(),
                                                        pageable)
                                        : serviceRepository.findActiveAfterCursorAsc(lastService.getCreatedAt(),
                                                        pageable);
                }

                boolean hasMore = services.size() > paginationDto.getSize();
                if (hasMore) {
                        services = services.subList(0, paginationDto.getSize());
                }
                String nextCursor = hasMore ? services.get(services.size() - 1).getId().toString() : null;

                List<ClinicServiceDto> records = services.stream()
                                .map(this::convertToPublicDto)
                                .collect(Collectors.toList());

                return PageResponse.<ClinicServiceDto>builder()
                                .records(records)
                                .build();
        }

        @Transactional(readOnly = true)
        public ClinicServiceDto getServiceById(UUID id) {
                log.info("Getting service by id: {}", id);
                com.clinicsystem.clinicapi.model.Service service = serviceRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                MessageCode.SERVICE_NOT_FOUND,
                                                "Service not found with id: " + id));

                if (!Boolean.TRUE.equals(service.getIsActive())) {
                        throw new ResourceNotFoundException(
                                        MessageCode.SERVICE_NOT_FOUND,
                                        "Service is not available");
                }

                return convertToPublicDto(service);
        }

        private ClinicServiceDto convertToPublicDto(com.clinicsystem.clinicapi.model.Service service) {
                SpecialtyDto specialtyDto = null;
                if (service.getSpecialty() != null) {
                        specialtyDto = SpecialtyDto.builder()
                                        .id(service.getSpecialty().getId())
                                        .name(service.getSpecialty().getName())
                                        .slug(service.getSpecialty().getSlug())
                                        .description(service.getSpecialty().getDescription())
                                        .image(service.getSpecialty().getImage())
                                        .build();
                }

                return ClinicServiceDto.builder()
                                .id(service.getId())
                                .name(service.getName())
                                .slug(service.getSlug())
                                .description(service.getDescription())
                                .price(service.getPrice())
                                .promotionalPrice(service.getPromotionalPrice())
                                .duration(service.getDuration())
                                .image(service.getImage())
                                .isFeatured(service.getIsFeatured())
                                .isActive(service.getIsActive())
                                .specialty(specialtyDto)
                                .build();
        }
}
