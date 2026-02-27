package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.SampleUserDto;
import com.clinicsystem.clinicapi.dto.SearchCriteria;
import com.clinicsystem.clinicapi.dto.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "Advanced search & filter examples")
public class SearchController {

    // Fake data for demonstration
    private static final List<SampleUserDto> FAKE_USERS = generateFakeUsers();

    @PostMapping("/users")
    @Operation(summary = "Search users with filters (fake data)", description = "Example: POST /api/v1/search/users with body: "
            +
            "{\"criteriaList\": [{\"key\": \"username\", \"operation\": \"like\", \"value\": \"user1\"}], " +
            "\"sortBy\": \"id\", \"sortDirection\": \"DESC\"}")
    public ResponseEntity<ApiResponse<List<SampleUserDto>>> searchUsers(
            @RequestBody SearchRequest searchRequest) {

        List<SampleUserDto> results = new ArrayList<>(FAKE_USERS);

        // Apply filters
        if (searchRequest.getCriteriaList() != null && !searchRequest.getCriteriaList().isEmpty()) {
            for (SearchCriteria criteria : searchRequest.getCriteriaList()) {
                results = applyFilter(results, criteria);
            }
        }

        // Apply sorting
        if (searchRequest.getSortBy() != null) {
            boolean ascending = !"DESC".equalsIgnoreCase(searchRequest.getSortDirection());
            results = sortResults(results, searchRequest.getSortBy(), ascending);
        }

        return ResponseEntity.ok(ApiResponse.success(MessageCode.SUCCESS, results));
    }

    @GetMapping("/users/example")
    @Operation(summary = "Search example with query params", description = "Example: /api/v1/search/users/example?username=user1&sortBy=id&sortDirection=DESC")
    public ResponseEntity<ApiResponse<List<SampleUserDto>>> searchUsersSimple(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {

        List<SampleUserDto> results = new ArrayList<>(FAKE_USERS);

        // Filter by username
        if (username != null && !username.isEmpty()) {
            results = results.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by email
        if (email != null && !email.isEmpty()) {
            results = results.stream()
                    .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Sort
        if (sortBy != null) {
            boolean ascending = "ASC".equalsIgnoreCase(sortDirection);
            results = sortResults(results, sortBy, ascending);
        }

        return ResponseEntity.ok(ApiResponse.success(MessageCode.SUCCESS, results));
    }

    private List<SampleUserDto> applyFilter(List<SampleUserDto> users, SearchCriteria criteria) {
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        String value = criteria.getValue() != null ? criteria.getValue().toString() : "";

        return users.stream().filter(user -> {
            switch (key) {
                case "username":
                    return applyStringFilter(user.getUsername(), operation, value);
                case "email":
                    return applyStringFilter(user.getEmail(), operation, value);
                case "fullName":
                    return applyStringFilter(user.getFullName(), operation, value);
                case "id":
                    return applyNumberFilter(user.getId(), operation, value);
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }

    private boolean applyStringFilter(String fieldValue, String operation, String filterValue) {
        if (fieldValue == null)
            return false;

        switch (operation.toLowerCase()) {
            case "eq":
                return fieldValue.equalsIgnoreCase(filterValue);
            case "like":
                return fieldValue.toLowerCase().contains(filterValue.toLowerCase());
            case "ne":
                return !fieldValue.equalsIgnoreCase(filterValue);
            default:
                return true;
        }
    }

    private boolean applyNumberFilter(Long fieldValue, String operation, String filterValue) {
        if (fieldValue == null)
            return false;

        try {
            long value = Long.parseLong(filterValue);
            switch (operation.toLowerCase()) {
                case "eq":
                    return fieldValue.equals(value);
                case "gt":
                    return fieldValue > value;
                case "lt":
                    return fieldValue < value;
                case "gte":
                    return fieldValue >= value;
                case "lte":
                    return fieldValue <= value;
                default:
                    return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private List<SampleUserDto> sortResults(List<SampleUserDto> users, String sortBy, boolean ascending) {
        return users.stream().sorted((u1, u2) -> {
            int comparison = 0;

            switch (sortBy) {
                case "id":
                    comparison = u1.getId().compareTo(u2.getId());
                    break;
                case "username":
                    comparison = u1.getUsername().compareTo(u2.getUsername());
                    break;
                case "email":
                    comparison = u1.getEmail().compareTo(u2.getEmail());
                    break;
                case "createdAt":
                    comparison = u1.getCreatedAt().compareTo(u2.getCreatedAt());
                    break;
                default:
                    return 0;
            }

            return ascending ? comparison : -comparison;
        }).collect(Collectors.toList());
    }

    private static List<SampleUserDto> generateFakeUsers() {
        List<SampleUserDto> users = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            users.add(SampleUserDto.builder()
                    .id((long) i)
                    .username("user" + i)
                    .email("user" + i + "@example.com")
                    .fullName("User Number " + i)
                    .createdAt(LocalDateTime.now().minusDays(50 - i))
                    .build());
        }
        return users;
    }
}
