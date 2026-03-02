package com.clinicsystem.clinicapi.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinicsystem.clinicapi.dto.HomePublicDto;
import com.clinicsystem.clinicapi.model.Specialty.SpecialtyType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;

    @Transactional(readOnly = true)
    public HomePublicDto getHomePublicData() {
        int totalPatients = patientService.getTotalPatients();
        int totalDoctors = doctorService.getTotalDoctors();
        int totalSpecialties = specialtyService.getTotalSpecialties();
        Map<SpecialtyType, Integer> doctorMap = specialtyService.getSpecialtiesGroupByType();

        return HomePublicDto.builder()
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalSpecialties(totalSpecialties)
                .doctorsMap(doctorMap)
                .build();
    }

}
