package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.clinicsystem.clinicapi.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@Tag(name = "Home", description = "Home endpoints for testing")
public class HomeController {

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieve all items from home endpoint")
    public ResponseEntity<ApiResponse<String>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.SUCCESS, "GET /api/v1/home - Route works!"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieve a specific item by ID")
    public ResponseEntity<ApiResponse<String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.SUCCESS,
                        "GET /api/v1/home/" + id + " - Route works!"));
    }

    @PostMapping
    @Operation(summary = "Create item", description = "Create a new item")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody(required = false) Object body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageCode.CREATED,
                        "POST /api/v1/home - Route works!"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "Update an existing item")
    public ResponseEntity<ApiResponse<String>> update(
            @PathVariable Long id,
            @RequestBody(required = false) Object body) {
        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.UPDATED,
                        "PUT /api/v1/home/" + id + " - Route works!"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Delete an item by ID")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(MessageCode.DELETED,
                        "DELETE /api/v1/home/" + id + " - Route works!"));
    }
}
