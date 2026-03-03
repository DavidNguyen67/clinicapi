package com.clinicsystem.clinicapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        // Define security scheme for JWT
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("Enter JWT Bearer token");

        // Define security scheme for API Key
        SecurityScheme apiKeySecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-KEY")
                .description("Enter API Key");

        return new OpenAPI()
                .info(new Info()
                        .title("Clinic Management System API")
                        .version("1.0.0")
                        .description("RESTful API for Clinic Management System - Quản lý phòng khám")
                        .contact(new Contact()
                                .name("Clinic System Team")
                                .email("info@abcclinic.com")
                                .url("https://abcclinic.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.abcclinic.com")
                                .description("Production Server")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", jwtSecurityScheme)
                        .addSecuritySchemes("API Key Authentication", apiKeySecurityScheme))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication")
                        .addList("API Key Authentication"));
    }
}
