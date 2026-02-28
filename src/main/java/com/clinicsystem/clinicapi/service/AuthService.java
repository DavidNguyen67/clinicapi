package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.LoginRequest;
import com.clinicsystem.clinicapi.dto.LoginResponse;
import com.clinicsystem.clinicapi.dto.RegisterRequest;
import com.clinicsystem.clinicapi.exception.BadRequestException;
import com.clinicsystem.clinicapi.model.User;
import com.clinicsystem.clinicapi.model.Role.RoleName;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(MessageCode.EMAIL_ALREADY_EXISTS,
                    "Email already exists: " + request.getEmail());
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException(MessageCode.PHONE_ALREADY_EXISTS,
                    "Phone already exists: " + request.getPhone());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setAvatar(request.getAvatar());
        user.setRole(RoleName.ROLE_PATIENT);
        user.setStatus(User.UserStatus.active);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        user = userRepository.save(user);
        log.info("Successfully registered user with ID: {}", user.getId());

        String accessToken = jwtUtil.generateTokenFromUsername(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getJwtExpiration() / 1000)
                .user(buildUserInfo(user))
                .build();
    }

    @Transactional
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
                .orElseThrow(() -> new BadRequestException(MessageCode.INVALID_CREDENTIALS,
                        "Invalid email or password"));

        // Check if user is active
        if (user.getStatus() != User.UserStatus.active) {
            throw new BadRequestException(MessageCode.ACCOUNT_INACTIVE,
                    "Account is not active");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtUtil.generateToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

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

    private LoginResponse.UserInfo buildUserInfo(User user) {
        return LoginResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }
}
