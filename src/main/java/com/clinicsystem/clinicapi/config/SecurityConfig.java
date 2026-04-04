package com.clinicsystem.clinicapi.config;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.clinicsystem.clinicapi.filter.ApiKeyAuthenticationFilter;
import com.clinicsystem.clinicapi.filter.JwtAuthenticationFilter;
import com.clinicsystem.clinicapi.service.CustomUserDetailsService;
import com.clinicsystem.clinicapi.util.EndpointSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
    private final EndpointSecurityUtil endpointSecurityUtil;
    private final ObjectMapper objectMapper;

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/home/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(endpointSecurityUtil::isPublic).permitAll()

                        // Swagger/OpenAPI endpoints
                        .requestMatchers("/api/v1/swagger-ui/**", "/api/v1/swagger-ui.html").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()

                        // // Admin endpoints
                        // .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // // Doctor endpoints
                        // .requestMatchers(HttpMethod.GET, "/api/doctors/**").authenticated()
                        // .requestMatchers(HttpMethod.POST, "/api/doctors/**").hasAnyRole("ADMIN",
                        // "STAFF")
                        // .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasAnyRole("ADMIN",
                        // "STAFF", "DOCTOR")
                        // .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")

                        // // Patient endpoints
                        // .requestMatchers(HttpMethod.GET, "/api/patients/**").authenticated()
                        // .requestMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("ADMIN",
                        // "STAFF")
                        // .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("ADMIN",
                        // "STAFF", "PATIENT")
                        // .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")

                        // // Appointment endpoints
                        // .requestMatchers("/api/appointments/**").authenticated()

                        // // All other requests
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, authException) -> writeSecurityErrorResponse(request, response,
                                        HttpStatus.UNAUTHORIZED,
                                        MessageCode.ERROR_UNAUTHORIZED,
                                        authException.getMessage()))
                        .accessDeniedHandler((request, response, accessDeniedException) -> writeSecurityErrorResponse(
                                request, response,
                                HttpStatus.FORBIDDEN,
                                MessageCode.ERROR_FORBIDDEN,
                                accessDeniedException.getMessage())))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(apiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept-Language"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void writeSecurityErrorResponse(HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String messageCode,
            String detail) throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("method", request.getMethod());
        errorDetails.put("detail", detail);

        String correlationId = response.getHeader("X-Correlation-ID");
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = request.getHeader("X-Correlation-ID");
        }
        if (correlationId != null && !correlationId.isBlank()) {
            errorDetails.put("correlationId", correlationId);
        }

        ApiResponse<Object> apiResponse = ApiResponse.error(messageCode, errorDetails);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
