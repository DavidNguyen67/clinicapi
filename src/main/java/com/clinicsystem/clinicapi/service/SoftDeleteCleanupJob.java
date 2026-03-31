package com.clinicsystem.clinicapi.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clinicsystem.clinicapi.repository.AppointmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoftDeleteCleanupJob {

    private final AppointmentRepository appointmentRepository;

    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(name = "cleanupSoftDeleted", lockAtMostFor = "10m", lockAtLeastFor = "1m")
    @Transactional
    public void cleanupSoftDeleted() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        int deleted = appointmentRepository.hardDeleteSoftDeleted(threshold);
        log.info("Cleaned up {} soft deleted appointments", deleted);

    }
}