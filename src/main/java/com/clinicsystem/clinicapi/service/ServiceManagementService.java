package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.ServicePublicDto;
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
public class ServiceManagementService {

    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public PageResponse<ServicePublicDto> getAllServices(
            UUID specialtyId,
            Boolean isFeatured,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.info("Getting services - specialtyId: {}, isFeatured: {}, page: {}, size: {}",
                specialtyId, isFeatured, page, size);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<com.clinicsystem.clinicapi.model.Service> servicePage = serviceRepository.findAll(pageable);

        List<ServicePublicDto> serviceDtos = servicePage.getContent().stream()
                .filter(service -> Boolean.TRUE.equals(service.getIsActive()))
                .filter(service -> specialtyId == null ||
                        (service.getSpecialty() != null && service.getSpecialty().getId().equals(specialtyId)))
                .filter(service -> isFeatured == null || service.getIsFeatured().equals(isFeatured))
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());

        return PageResponse.<ServicePublicDto>builder()
                .records(serviceDtos)
                .nextCursor(servicePage.hasNext() ? String.valueOf(page + 1) : null)
                .hasMore(servicePage.hasNext())
                .pageSize(servicePage.getSize())
                .pageNumber(servicePage.getNumber())
                .totalPages(servicePage.getTotalPages())
                .totalElements(servicePage.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public ServicePublicDto getServiceById(UUID id) {
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

    private ServicePublicDto convertToPublicDto(com.clinicsystem.clinicapi.model.Service service) {
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

        return ServicePublicDto.builder()
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
