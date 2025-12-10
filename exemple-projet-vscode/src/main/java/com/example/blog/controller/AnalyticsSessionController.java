package com.example.blog.controller;

import com.example.blog.dto.AnalyticsSessionCreateDto;
import com.example.blog.dto.AnalyticsSessionReadDto;
import com.example.blog.dto.AnalyticsSessionUpdateDto;
import com.example.blog.service.AnalyticsSessionService;
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
@RequestMapping("/api/v1/analyticssessions")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsSessionController {

    private final AnalyticsSessionService service;

    @GetMapping
    public ResponseEntity<Page<AnalyticsSessionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/analyticssessions - Finding all AnalyticsSessions");
        Page<AnalyticsSessionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsSessionReadDto>> findAllList() {
        log.info("GET /api/v1/analyticssessions/all - Finding all AnalyticsSessions as list");
        List<AnalyticsSessionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsSessionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/analyticssessions/{} - Finding AnalyticsSession by id", id);
        AnalyticsSessionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AnalyticsSessionReadDto> create(@Valid @RequestBody AnalyticsSessionCreateDto createDto) {
        log.info("POST /api/v1/analyticssessions - Creating new AnalyticsSession: {}", createDto);
        AnalyticsSessionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticsSessionReadDto> update(@PathVariable Long id, @Valid @RequestBody AnalyticsSessionUpdateDto updateDto) {
        log.info("PUT /api/v1/analyticssessions/{} - Updating AnalyticsSession: {}", id, updateDto);
        AnalyticsSessionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/analyticssessions/{} - Deleting AnalyticsSession", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AnalyticsSessionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/analyticssessions/{}/suspend - Suspending AnalyticsSession", id);
        AnalyticsSessionReadDto suspended = service.suspendAnalyticsSession(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AnalyticsSessionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/analyticssessions/{}/activate - Activating AnalyticsSession", id);
        AnalyticsSessionReadDto activated = service.activateAnalyticsSession(id);
        return ResponseEntity.ok(activated);
    }

}
