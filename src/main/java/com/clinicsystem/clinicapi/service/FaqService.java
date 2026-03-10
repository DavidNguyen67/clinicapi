package com.clinicsystem.clinicapi.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.FaqDto;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.PaginationDto;
import com.clinicsystem.clinicapi.exception.ResourceNotFoundException;
import com.clinicsystem.clinicapi.model.Faq;
import com.clinicsystem.clinicapi.repository.FaqRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
        private final FaqRepository faqRepository;

        @Transactional(readOnly = true)
        public PageResponse<FaqDto> getAllFaqs(PaginationDto paginationDto) {
                int limit = paginationDto.getSize() + 1;
                PageRequest pageable = PageRequest.of(0, limit);

                List<Faq> faqs;
                if (paginationDto.getLastId() == null || paginationDto.getLastId().isBlank()) {
                        faqs = faqRepository.findActiveForFirstPage(true, pageable);
                } else {
                        UUID lastId = UUID.fromString(paginationDto.getLastId());
                        Faq lastFaq = faqRepository.findById(lastId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        MessageCode.FAQ_NOT_FOUND, "Cursor not found"));
                        faqs = faqRepository.getAllFaqs(
                                        true, lastFaq.getCreatedAt(), pageable);
                }

                boolean hasMore = faqs.size() > paginationDto.getSize();
                if (hasMore) {
                        faqs = faqs.subList(0, paginationDto.getSize());
                }

                List<FaqDto> records = faqs.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return PageResponse.<FaqDto>builder()
                                .records(records)
                                .hasMore(hasMore)
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
