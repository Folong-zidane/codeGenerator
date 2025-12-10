package com.example.blog.controller;

import com.example.blog.dto.AnalyticsEventCreateDto;
import com.example.blog.dto.AnalyticsEventReadDto;
import com.example.blog.dto.AnalyticsEventUpdateDto;
import com.example.blog.service.AnalyticsEventService;
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
@RequestMapping("/api/v1/analyticsevents")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventController {

    private final AnalyticsEventService service;

    @GetMapping
    public ResponseEntity<Page<AnalyticsEventReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/analyticsevents - Finding all AnalyticsEvents");
        Page<AnalyticsEventReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsEventReadDto>> findAllList() {
        log.info("GET /api/v1/analyticsevents/all - Finding all AnalyticsEvents as list");
        List<AnalyticsEventReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsEventReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/analyticsevents/{} - Finding AnalyticsEvent by id", id);
        AnalyticsEventReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AnalyticsEventReadDto> create(@Valid @RequestBody AnalyticsEventCreateDto createDto) {
        log.info("POST /api/v1/analyticsevents - Creating new AnalyticsEvent: {}", createDto);
        AnalyticsEventReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticsEventReadDto> update(@PathVariable Long id, @Valid @RequestBody AnalyticsEventUpdateDto updateDto) {
        log.info("PUT /api/v1/analyticsevents/{} - Updating AnalyticsEvent: {}", id, updateDto);
        AnalyticsEventReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/analyticsevents/{} - Deleting AnalyticsEvent", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AnalyticsEventReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/analyticsevents/{}/suspend - Suspending AnalyticsEvent", id);
        AnalyticsEventReadDto suspended = service.suspendAnalyticsEvent(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AnalyticsEventReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/analyticsevents/{}/activate - Activating AnalyticsEvent", id);
        AnalyticsEventReadDto activated = service.activateAnalyticsEvent(id);
        return ResponseEntity.ok(activated);
    }

}
