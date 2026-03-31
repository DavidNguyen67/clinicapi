package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ChangePasswordRequest;
import com.clinicsystem.clinicapi.dto.ForgotPasswordRequest;
import com.clinicsystem.clinicapi.dto.LoginRequest;
import com.clinicsystem.clinicapi.dto.LoginResponse;
import com.clinicsystem.clinicapi.dto.LoginResponse.UserInfo;
import com.clinicsystem.clinicapi.dto.RegisterRequest;
import com.clinicsystem.clinicapi.dto.ResetPasswordRequest;
import com.clinicsystem.clinicapi.exception.BadRequestException;
import com.clinicsystem.clinicapi.model.PasswordResetToken;
import com.clinicsystem.clinicapi.model.User;
import com.clinicsystem.clinicapi.model.Role.RoleName;
import com.clinicsystem.clinicapi.repository.PasswordResetTokenRepository;
import com.clinicsystem.clinicapi.repository.UserRepository;
import com.clinicsystem.clinicapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordResetTokenRepository passwordResetTokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;
        private final PatientService patientService;
        private final EmailBloomFilter emailBloomFilter;
        private final PhoneBloomFilter phoneBloomFilter;

        @Transactional(rollbackFor = Exception.class)
        public LoginResponse register(RegisterRequest request) {
                log.info("Registering new user with email: {}", request.getEmail());

                User user = new User();
                user.setEmail(request.getEmail());
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                user.setPhone(request.getPhone());
                user.setFullName(request.getFullName());
                user.setDateOfBirth(request.getDateOfBirth());
                user.setPathAvatar(request.getPathAvatar());
                user.setRole(RoleName.ROLE_PATIENT);
                user.setStatus(User.UserStatus.ACTIVE);
                user.setEmailVerified(false);
                user.setPhoneVerified(false);

                user = userRepository.save(user);
                emailBloomFilter.add(user.getEmail());
                phoneBloomFilter.add(user.getPhone());
                log.info("Successfully registered user with ID: {}", user.getId());

                // Automatically create Patient for new user
                patientService.createPatient(user);
                log.info("Successfully created patient for user: {}", user.getEmail());

                // Generate tokens with full user information
                String accessToken = jwtUtil.generateToken(
                                user);
                String refreshToken = jwtUtil.generateRefreshToken(user.getId());

                emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtUtil.getJwtExpiration() / 1000)
                                .user(buildUserInfo(user))
                                .build();
        }

        @Transactional(rollbackFor = Exception.class)
        public LoginResponse login(LoginRequest request) {
                log.info("User login attempt with email: {}", request.getEmail());

                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Get user from database
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BadRequestException(MessageCode.USER_INVALID_CREDENTIALS,
                                                "Invalid email or password"));

                // Check if user is active
                if (user.getStatus() != User.UserStatus.ACTIVE) {
                        throw new BadRequestException(MessageCode.USER_ACCOUNT_INACTIVE,
                                        "Account is not active");
                }

                // Update last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                // Generate tokens with full user information
                String accessToken = jwtUtil.generateToken(user);
                String refreshToken = jwtUtil.generateRefreshToken(user.getId());

                log.info("User logged in successfully with email: {}", request.getEmail());

                // Build response
                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtUtil.getJwtExpiration() / 1000) // Convert to seconds
                                .user(buildUserInfo(user))
                                .build();
        }

        @Transactional(rollbackFor = Exception.class)
        public LoginResponse refreshToken(String refreshToken) {
                log.info("Refresh token request");

                // Validate refresh token
                if (!jwtUtil.validateRefreshToken(refreshToken)) {
                        throw new BadRequestException(MessageCode.AUTH_REFRESH_TOKEN_INVALID,
                                        "Invalid or expired refresh token");
                }

                // Get user ID from token
                String userId = jwtUtil.getUserIdFromToken(refreshToken);

                // Get user from database
                User user = userRepository.findById(UUID.fromString(userId))
                                .orElseThrow(() -> new BadRequestException(MessageCode.USER_NOT_FOUND,
                                                "User not found"));

                // Check if user is active
                if (user.getStatus() != User.UserStatus.ACTIVE) {
                        throw new BadRequestException(MessageCode.USER_ACCOUNT_INACTIVE,
                                        "Account is not active");
                }

                // Generate new tokens
                String newAccessToken = jwtUtil.generateToken(user);
                String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

                log.info("Successfully refreshed token for user: {}", user.getEmail());

                return LoginResponse.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(newRefreshToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtUtil.getJwtExpiration() / 1000)
                                .user(buildUserInfo(user))
                                .build();
        }

        private UserInfo buildUserInfo(User user) {
                return LoginResponse.UserInfo.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .fullName(user.getFullName())
                                .pathAvatar(user.getPathAvatar())
                                .role(user.getRole().name())
                                .status(user.getStatus().name())
                                .build();
        }

        @Transactional(rollbackFor = Exception.class)
        public void changePassword(String email, ChangePasswordRequest request) {
                log.info("Change password request for user: {}", email);

                // Get user from database
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new BadRequestException(MessageCode.USER_NOT_FOUND,
                                                "User not found"));

                // Verify current password
                if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
                        throw new BadRequestException(MessageCode.PASSWORD_CURRENT_INCORRECT,
                                        "Current password is incorrect");
                }

                // Check if new password is same as current
                if (request.getCurrentPassword().equals(request.getNewPassword())) {
                        throw new BadRequestException(MessageCode.PASSWORD_MISMATCH,
                                        "New password must be different from current password");
                }

                // Update password
                user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);

                log.info("Password changed successfully for user: {}", email);
        }

        @Transactional(rollbackFor = Exception.class)
        public void forgotPassword(ForgotPasswordRequest request) {
                log.info("Forgot password request for email: {}", request.getEmail());

                // Get user by email
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BadRequestException(MessageCode.USER_NOT_FOUND,
                                                "User with this email does not exist"));

                // Delete any existing reset tokens for this user
                passwordResetTokenRepository.deleteByUser(user);

                // Generate reset token (6-digit random code or UUID)
                String resetToken = UUID.randomUUID().toString();

                int expiryHours = 1; // Token valid for 1 hour

                // Create password reset token (valid for 1 hour)
                PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                                .user(user)
                                .token(resetToken)
                                .expiryDate(LocalDateTime.now().plusHours(expiryHours))
                                .used(false)
                                .build();

                passwordResetTokenRepository.save(passwordResetToken);

                // TODO: Send email with reset token
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken, expiryHours);
                log.info("Password reset token generated for user: {}", user.getEmail());

                // For development/testing purposes, log the token
                log.info("Reset token for {}: {}", request.getEmail(), resetToken);
        }

        @Transactional(rollbackFor = Exception.class)
        public void resetPassword(ResetPasswordRequest request) {
                log.info("Reset password request with token");

                // Find token
                PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                                .orElseThrow(() -> new BadRequestException(MessageCode.PASSWORD_RESET_TOKEN_INVALID,
                                                "Invalid password reset token"));

                // Check if token is expired
                if (resetToken.isExpired()) {
                        throw new BadRequestException(MessageCode.PASSWORD_RESET_TOKEN_EXPIRED,
                                        "Password reset token has expired");
                }

                // Check if token is already used
                if (resetToken.getUsed()) {
                        throw new BadRequestException(MessageCode.PASSWORD_RESET_TOKEN_USED,
                                        "Password reset token has already been used");
                }

                // Get user and update password
                User user = resetToken.getUser();
                user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);

                // Mark token as used
                resetToken.setUsed(true);
                passwordResetTokenRepository.save(resetToken);

                log.info("Password reset successfully for user: {}", user.getEmail());
        }
}
