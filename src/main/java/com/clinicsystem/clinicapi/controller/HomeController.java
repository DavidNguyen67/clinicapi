package com.clinicsystem.clinicapi.controller;

import com.clinicsystem.clinicapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse<String>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("GET /api/home - Route works!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("GET /api/home/" + id + " - Route works!"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody(required = false) Object body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("POST /api/home - Route works!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> update(
            @PathVariable Long id,
            @RequestBody(required = false) Object body) {
        return ResponseEntity.ok(ApiResponse.success("PUT /api/home/" + id + " - Route works!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("DELETE /api/home/" + id + " - Route works!"));
    }
}
