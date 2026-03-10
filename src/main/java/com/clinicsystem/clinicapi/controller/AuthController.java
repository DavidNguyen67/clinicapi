package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.ChangePasswordRequest;
import com.clinicsystem.clinicapi.dto.ForgotPasswordRequest;
import com.clinicsystem.clinicapi.dto.LoginRequest;
import com.clinicsystem.clinicapi.dto.LoginResponse;
import com.clinicsystem.clinicapi.dto.RegisterRequest;
import com.clinicsystem.clinicapi.dto.ResetPasswordRequest;
import com.clinicsystem.clinicapi.service.AuthService;
import com.clinicsystem.clinicapi.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
        private final AuthService authService;
        private final JwtUtil jwtUtil;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<LoginResponse>> register(
                        @Valid @RequestBody RegisterRequest request,
                        HttpServletResponse httpResponse) {
                log.info("POST /api/v1/auth/register - Register new user with email: {}", request.getEmail());

                LoginResponse response = authService.register(request);

                // Set refresh token in HttpOnly cookie
                setRefreshTokenCookie(httpResponse, response.getRefreshToken());

                // Don't send refresh token in response body
                response.setRefreshToken(null);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.<LoginResponse>builder()
                                                .success(true)
                                                .messageCode(MessageCode.AUTH_REGISTER_SUCCESS)
                                                .data(response)
                                                .build());
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponse>> login(
                        @Valid @RequestBody LoginRequest request,
                        HttpServletResponse httpResponse) {
                log.info("POST /api/v1/auth/login - Login attempt with email: {}", request.getEmail());

                LoginResponse response = authService.login(request);

                // Set refresh token in HttpOnly cookie
                setRefreshTokenCookie(httpResponse, response.getRefreshToken());

                // Don't send refresh token in response body
                response.setRefreshToken(null);

                return ResponseEntity
                                .ok(ApiResponse.<LoginResponse>builder()
                                                .success(true)
                                                .messageCode(MessageCode.AUTH_LOGIN_SUCCESS)
                                                .data(response)
                                                .build());
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse httpResponse) {
                log.info("POST /api/v1/auth/logout - User logout");

                // Clear refresh token cookie
                clearRefreshTokenCookie(httpResponse);

                return ResponseEntity
                                .ok(ApiResponse.<Void>builder()
                                                .success(true)
                                                .messageCode(MessageCode.AUTH_LOGOUT_SUCCESS)
                                                .build());
        }

        @PostMapping("/refresh")
        public ResponseEntity<ApiResponse<LoginResponse>> refresh(
                        HttpServletRequest httpRequest,
                        HttpServletResponse httpResponse) {
                log.info("POST /api/v1/auth/refresh - Refresh token request");

                // Get refresh token from cookie
                String refreshToken = getRefreshTokenFromCookie(httpRequest);

                if (refreshToken == null) {
                        throw new com.clinicsystem.clinicapi.exception.BadRequestException(
                                        MessageCode.AUTH_REFRESH_TOKEN_INVALID,
                                        "Refresh token not found in cookie");
                }

                LoginResponse response = authService.refreshToken(refreshToken);

                // Set new refresh token in cookie
                setRefreshTokenCookie(httpResponse, response.getRefreshToken());

                // Don't send refresh token in response body
                response.setRefreshToken(null);

                return ResponseEntity
                                .ok(ApiResponse.<LoginResponse>builder()
                                                .success(true)
                                                .messageCode(MessageCode.AUTH_REFRESH_SUCCESS)
                                                .data(response)
                                                .build());
        }

        @PostMapping("/change-password")
        public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
                log.info("POST /api/v1/auth/change-password - Change password request");

                // Get authenticated user's email from SecurityContext
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                authService.changePassword(email, request);

                return ResponseEntity
                                .ok(ApiResponse.<Void>builder()
                                                .success(true)
                                                .messageCode(MessageCode.PASSWORD_CHANGED_SUCCESS)
                                                .build());
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
                log.info("POST /api/v1/auth/forgot-password - Forgot password request for email: {}",
                                request.getEmail());

                authService.forgotPassword(request);

                return ResponseEntity
                                .ok(ApiResponse.<Void>builder()
                                                .success(true)
                                                .messageCode(MessageCode.PASSWORD_RESET_EMAIL_SENT)
                                                .build());
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
                log.info("POST /api/v1/auth/reset-password - Reset password request");

                authService.resetPassword(request);

                return ResponseEntity
                                .ok(ApiResponse.<Void>builder()
                                                .success(true)
                                                .messageCode(MessageCode.PASSWORD_RESET_SUCCESS)
                                                .build());
        }

        private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
                Cookie cookie = new Cookie(jwtUtil.getRefreshCookieName(), refreshToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true); // Only over HTTPS
                cookie.setPath("/api/v1/auth");
                cookie.setMaxAge((int) (jwtUtil.getRefreshExpiration() / 1000)); // Convert to seconds
                // cookie.setSameSite("Strict"); // Not available in Jakarta Servlet yet, handle
                // via header
                response.addCookie(cookie);
                // Set SameSite via header for better security
                response.addHeader("Set-Cookie",
                                String.format("%s=%s; Path=/api/v1/auth; HttpOnly; Secure; SameSite=Strict; Max-Age=%d",
                                                jwtUtil.getRefreshCookieName(), refreshToken,
                                                (int) (jwtUtil.getRefreshExpiration() / 1000)));
        }

        private void clearRefreshTokenCookie(HttpServletResponse response) {
                Cookie cookie = new Cookie(jwtUtil.getRefreshCookieName(), "");
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/api/v1/auth");
                cookie.setMaxAge(0); // Delete cookie
                response.addCookie(cookie);
        }

        private String getRefreshTokenFromCookie(HttpServletRequest request) {
                if (request.getCookies() == null) {
                        return null;
                }
                return Arrays.stream(request.getCookies())
                                .filter(cookie -> jwtUtil.getRefreshCookieName().equals(cookie.getName()))
                                .findFirst()
                                .map(Cookie::getValue)
                                .orElse(null);
        }
}
