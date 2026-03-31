package com.clinicsystem.clinicapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.CreateAppointmentRequest;
import com.clinicsystem.clinicapi.dto.UpdateAppointmentRequest;
import com.clinicsystem.clinicapi.dto.AppointmentResponseDto;
import com.clinicsystem.clinicapi.service.BookingService;
import com.clinicsystem.clinicapi.validation.Public;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @Public
    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest createAppointmentRequest) {

        AppointmentResponseDto appointment = bookingService.publishAppointmentEvent(createAppointmentRequest);

        return ResponseEntity.ok(ApiResponse.<AppointmentResponseDto>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(appointment)
                .build());
    }

    @PutMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointment(
            @Valid @RequestBody UpdateAppointmentRequest updateAppointmentRequest) {

        AppointmentResponseDto appointment = bookingService.updateAppointment(updateAppointmentRequest);

        return ResponseEntity.ok(ApiResponse.<AppointmentResponseDto>builder()
                .success(true)
                .messageCode(MessageCode.GENERAL_SUCCESS)
                .data(appointment)
                .build());
    }

}
