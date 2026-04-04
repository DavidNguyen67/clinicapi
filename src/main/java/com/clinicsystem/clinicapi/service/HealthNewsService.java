package com.clinicsystem.clinicapi.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clinicsystem.clinicapi.dto.HealthNewsArticleDto;
import com.clinicsystem.clinicapi.dto.HealthNewsResponseDto;
import com.clinicsystem.clinicapi.dto.HealthNewsSourceDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HealthNewsService {
    private static final int MIN_MAX = 1;
    private static final int MAX_ALLOWED = 25;

    private final ObjectMapper objectMapper;

    @Value("${integrations.gnews.base-url}")
    private String gnewsBaseUrl;

    @Value("${integrations.gnews.apikey}")
    private String gnewsApiKey;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public HealthNewsResponseDto getTopHealthNews(String lang, String country, int max) {
        if (gnewsApiKey == null || gnewsApiKey.isBlank()) {
            throw new IllegalStateException("GNews API key is missing. Set GNEWS_API_KEY environment variable.");
        }

        int safeMax = Math.max(MIN_MAX, Math.min(max, MAX_ALLOWED));

        String url = buildTopHeadlinesUrl(lang, country, safeMax);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new IllegalStateException(
                        "Failed to fetch health news from GNews. HTTP status: " + response.statusCode());
            }

            return mapResponse(response.body());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Health news request was interrupted", ex);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot fetch health news from GNews", ex);
        }
    }

    private String buildTopHeadlinesUrl(String lang, String country, int max) {
        String safeLang = sanitizeOrDefault(lang, "vi");
        String safeCountry = sanitizeOrDefault(country, "vn");

        return gnewsBaseUrl + "/top-headlines"
                + "?topic=health"
                + "&lang=" + encode(safeLang)
                + "&country=" + encode(safeCountry)
                + "&max=" + max
                + "&apikey=" + encode(gnewsApiKey);
    }

    private String sanitizeOrDefault(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private HealthNewsResponseDto mapResponse(String rawBody) throws IOException {
        JsonNode root = objectMapper.readTree(rawBody);
        int totalArticles = root.path("totalArticles").asInt(0);

        List<HealthNewsArticleDto> articles = new ArrayList<>();
        for (JsonNode articleNode : root.path("articles")) {
            JsonNode sourceNode = articleNode.path("source");
            articles.add(HealthNewsArticleDto.builder()
                    .title(asNullableText(articleNode.path("title")))
                    .description(asNullableText(articleNode.path("description")))
                    .content(asNullableText(articleNode.path("content")))
                    .url(asNullableText(articleNode.path("url")))
                    .image(asNullableText(articleNode.path("image")))
                    .publishedAt(asNullableText(articleNode.path("publishedAt")))
                    .lang(asNullableText(articleNode.path("lang")))
                    .source(mapSource(sourceNode))
                    .build());
        }

        return HealthNewsResponseDto.builder()
                .totalArticles(totalArticles)
                .articles(articles)
                .build();
    }

    private HealthNewsSourceDto mapSource(JsonNode sourceNode) {
        if (sourceNode == null || sourceNode.isMissingNode() || sourceNode.isNull()) {
            return null;
        }

        String id = asNullableText(sourceNode.path("id"));
        String name = asNullableText(sourceNode.path("name"));
        String url = asNullableText(sourceNode.path("url"));

        if (id == null && name == null && url == null) {
            return null;
        }

        return HealthNewsSourceDto.builder()
                .id(id)
                .name(name)
                .url(url)
                .build();
    }

    private String asNullableText(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        String value = node.asText();
        return value.isBlank() ? null : value;
    }
}
