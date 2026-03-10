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
public class ClinicServiceService {

        private final ServiceRepository serviceRepository;

        @Transactional(readOnly = true)
        public PageResponse<ClinicServiceDto> getAllServices(
                        PaginationDto paginationDto) {

                Sort sort = Sort.by(Sort.Direction.fromString(paginationDto.getSortDirection()),
                                paginationDto.getSortBy());
                Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize(), sort);

                Page<com.clinicsystem.clinicapi.model.Service> servicePage = serviceRepository
                                .getAllActiveServices(pageable);

                List<ClinicServiceDto> serviceDtos = servicePage.getContent().stream()
                                .map(this::convertToPublicDto)
                                .collect(Collectors.toList());

                return PageResponse.<ClinicServiceDto>builder()
                                .records(serviceDtos)
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
