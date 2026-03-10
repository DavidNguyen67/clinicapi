package com.clinicsystem.clinicapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicsystem.clinicapi.dto.FaqDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.repository.FaqRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    @Transactional(readOnly = true)
    public PageResponse<FaqDto> getAllFaq(PaginationDto paginationDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationDto.getSortDirection()),
                paginationDto.getSortBy());
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize(), sort);
        Page<com.clinicsystem.clinicapi.model.Faq> faqPage = faqRepository
                .getAllActiveFaq(pageable);

        List<FaqDto> faqDtos = faqPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return PageResponse.<FaqDto>builder()
                .records(faqDtos)
                .build();
    }

    private FaqDto convertToDto(com.clinicsystem.clinicapi.model.Faq faq) {
        return FaqDto.builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .isActive(faq.getIsActive())
                .build();
    }

}
