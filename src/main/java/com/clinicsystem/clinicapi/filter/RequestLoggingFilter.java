package com.clinicsystem.clinicapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Generate or extract correlation ID
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        // Set correlation ID in MDC for logging
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

        // Add correlation ID to response header
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        // Wrap request and response to enable multiple reads
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // Log request
            logRequest(requestWrapper, correlationId);

            // Continue filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);

        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Log response
            logResponse(responseWrapper, correlationId, duration);

            // Copy response body to actual response
            responseWrapper.copyBodyToResponse();

            // Clear MDC
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String correlationId) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;

        log.info("[REQUEST] [{}] {} {} - Client: {}",
                correlationId,
                method,
                fullUrl,
                request.getRemoteAddr());
    }

    private void logResponse(ContentCachingResponseWrapper response, String correlationId, long duration) {
        int status = response.getStatus();
        String level = status >= 400 ? "ERROR" : "INFO";

        String logMessage = String.format("[RESPONSE] [%s] Status: %d - Duration: %dms",
                correlationId,
                status,
                duration);

        if ("ERROR".equals(level)) {
            log.error(logMessage);
        } else {
            log.info(logMessage);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip logging for static resources and actuator endpoints
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars");
    }
}
