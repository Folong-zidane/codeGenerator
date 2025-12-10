package com.example.blog.controller;

import com.example.blog.dto.RateLimitCreateDto;
import com.example.blog.dto.RateLimitReadDto;
import com.example.blog.dto.RateLimitUpdateDto;
import com.example.blog.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ratelimits")
@RequiredArgsConstructor
@Slf4j
public class RateLimitController {

    private final RateLimitService service;

    @GetMapping
    public ResponseEntity<Page<RateLimitReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/ratelimits - Finding all RateLimits");
        Page<RateLimitReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RateLimitReadDto>> findAllList() {
        log.info("GET /api/v1/ratelimits/all - Finding all RateLimits as list");
        List<RateLimitReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/ratelimits/{} - Finding RateLimit by id", id);
        RateLimitReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RateLimitReadDto> create(@Valid @RequestBody RateLimitCreateDto createDto) {
        log.info("POST /api/v1/ratelimits - Creating new RateLimit: {}", createDto);
        RateLimitReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RateLimitReadDto> update(@PathVariable Long id, @Valid @RequestBody RateLimitUpdateDto updateDto) {
        log.info("PUT /api/v1/ratelimits/{} - Updating RateLimit: {}", id, updateDto);
        RateLimitReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/ratelimits/{} - Deleting RateLimit", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<RateLimitReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/ratelimits/{}/suspend - Suspending RateLimit", id);
        RateLimitReadDto suspended = service.suspendRateLimit(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RateLimitReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/ratelimits/{}/activate - Activating RateLimit", id);
        RateLimitReadDto activated = service.activateRateLimit(id);
        return ResponseEntity.ok(activated);
    }

}
