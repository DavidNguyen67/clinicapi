package com.clinicsystem.clinicapi.filter;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.exception.InvalidApiKeyException;
import com.clinicsystem.clinicapi.util.EndpointSecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper;
    private final EndpointSecurityUtil endpointSecurityUtil;

    @Value("${api.security.keys}")
    private String[] validApiKeys;

    @Value("${api.security.enabled}")
    private boolean apiKeyEnabled;

    // Public endpoints that don't require API key
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/swagger-ui/**",
            "/api/v1/swagger-ui.html");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Skip validation if API key security is disabled
        if (!apiKeyEnabled) {
            filterChain.doFilter(request, response);
            return;
        }

        if (endpointSecurityUtil.isPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestPath = request.getRequestURI();

        // Skip validation for public endpoints
        if (isPublicEndpoint(requestPath)) {
            log.debug("Skipping API key validation for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        // Extract API key from header
        String apiKey = request.getHeader(API_KEY_HEADER);

        try {
            // Validate API key
            if (!StringUtils.hasText(apiKey)) {
                log.warn("Missing API key for request: {} from IP: {}", requestPath, getClientIp(request));
                throw new InvalidApiKeyException("API key is missing");
            }

            if (!isValidApiKey(apiKey)) {
                log.warn("Invalid API key attempt for request: {} from IP: {}", requestPath, getClientIp(request));
                throw new InvalidApiKeyException("API key is invalid");
            }

            log.debug("Valid API key for request: {}", requestPath);
            filterChain.doFilter(request, response);

        } catch (InvalidApiKeyException ex) {
            handleInvalidApiKey(response, ex);
        }
    }

    private boolean isPublicEndpoint(String requestPath) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    private boolean isValidApiKey(String apiKey) {
        if (validApiKeys == null || validApiKeys.length == 0) {
            log.warn("No valid API keys configured. Check application.yml");
            return false;
        }
        return Arrays.asList(validApiKeys).contains(apiKey);
    }

    private void handleInvalidApiKey(HttpServletResponse response, InvalidApiKeyException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String messageCode = ex.getMessage().contains("missing")
                ? MessageCode.API_KEY_MISSING
                : MessageCode.API_KEY_INVALID;

        ApiResponse<Object> apiResponse = ApiResponse.error(messageCode);

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
