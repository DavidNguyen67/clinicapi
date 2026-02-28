package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.LoginRequest;
import com.clinicsystem.clinicapi.dto.LoginResponse;
import com.clinicsystem.clinicapi.dto.RegisterRequest;
import com.clinicsystem.clinicapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/v1/auth/register - Register new user with email: {}", request.getEmail());

        LoginResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .messageCode(MessageCode.AUTH_REGISTER_SUCCESS)
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - Login attempt with email: {}", request.getEmail());

        LoginResponse response = authService.login(request);

        return ResponseEntity
                .ok(ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .messageCode(MessageCode.AUTH_LOGIN_SUCCESS)
                        .data(response)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        log.info("POST /api/auth/logout - User logout");
        return ResponseEntity
                .ok(ApiResponse.<Void>builder()
                        .success(true).messageCode(MessageCode.AUTH_LOGOUT_SUCCESS)
                        .build());
    }
}
