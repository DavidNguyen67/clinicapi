package com.clinicsystem.clinicapi.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.clinicsystem.clinicapi.validation.Public;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class EndpointSecurityUtil {

    private final RequestMappingHandlerMapping handlerMapping;

    public EndpointSecurityUtil(
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public boolean isPublic(HttpServletRequest request) {
        try {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);

            if (handler == null)
                return false;

            Object handlerObj = handler.getHandler();

            if (handlerObj instanceof HandlerMethod method) {
                return method.hasMethodAnnotation(Public.class)
                        || method.getBeanType().isAnnotationPresent(Public.class);
            }

        } catch (Exception ignored) {
        }

        return false;
    }
}
