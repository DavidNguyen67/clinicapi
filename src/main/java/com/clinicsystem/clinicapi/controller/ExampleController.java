package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import com.clinicsystem.clinicapi.dto.PageResponse;
import com.clinicsystem.clinicapi.dto.SampleUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/examples")
@Tag(name = "Examples", description = "Example endpoints showing standardized patterns")
public class ExampleController {

    // Fake data for demonstration
    private static final List<SampleUserDto> FAKE_USERS = generateFakeUsers();

    @GetMapping("/users")
    @Operation(summary = "Get paginated users (fake data)", description = "Example of cursor-based pagination with fake data. Use cursor from response for next page.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<PageResponse<SampleUserDto>>> getPaginatedUsers(
            @Parameter(description = "Cursor for pagination (base64 encoded ID)") @RequestParam(required = false) String cursor,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int limit) {

        // Parse cursor (in real implementation, this would be decoded)
        long startId = 0;
        if (cursor != null && !cursor.isEmpty()) {
            try {
                String decodedCursor = new String(Base64.getDecoder().decode(cursor));
                startId = Long.parseLong(decodedCursor);
            } catch (Exception e) {
                startId = 0;
            }
        }

        // Get paginated data
        List<SampleUserDto> paginatedUsers = new ArrayList<>();
        for (SampleUserDto user : FAKE_USERS) {
            if (user.getId() > startId) {
                paginatedUsers.add(user);
                if (paginatedUsers.size() >= limit) {
                    break;
                }
            }
        }

        // Determine if there are more items
        boolean hasMore = false;
        String nextCursor = null;
        if (!paginatedUsers.isEmpty()) {
            long lastId = paginatedUsers.get(paginatedUsers.size() - 1).getId();
            hasMore = FAKE_USERS.stream().anyMatch(u -> u.getId() > lastId);
            if (hasMore) {
                nextCursor = Base64.getEncoder().encodeToString(String.valueOf(lastId).getBytes());
            }
        }

        PageResponse<SampleUserDto> pageResponse = PageResponse.of(
                paginatedUsers,
                nextCursor,
                hasMore,
                limit,
                FAKE_USERS.size());

        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.SUCCESS, pageResponse));
    }

    @GetMapping("/test-message-codes")
    @Operation(summary = "Test message codes", description = "Demo of client-side i18n with message codes")
    public ResponseEntity<ApiResponse<String>> testMessageCodes() {
        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.SUCCESS,
                        "Client will map messageCode to localized text"));
    }

    @GetMapping("/test-error")
    @Operation(summary = "Test error response", description = "Test standardized error response format")
    public ResponseEntity<ApiResponse<Object>> testError() {
        throw new RuntimeException("This is a test error");
    }

    // Generate fake users for demonstration
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
