package com.example.blog.controller;

import com.example.blog.dto.MediaUsageCreateDto;
import com.example.blog.dto.MediaUsageReadDto;
import com.example.blog.dto.MediaUsageUpdateDto;
import com.example.blog.service.MediaUsageService;
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
@RequestMapping("/api/v1/mediausages")
@RequiredArgsConstructor
@Slf4j
public class MediaUsageController {

    private final MediaUsageService service;

    @GetMapping
    public ResponseEntity<Page<MediaUsageReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/mediausages - Finding all MediaUsages");
        Page<MediaUsageReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaUsageReadDto>> findAllList() {
        log.info("GET /api/v1/mediausages/all - Finding all MediaUsages as list");
        List<MediaUsageReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaUsageReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/mediausages/{} - Finding MediaUsage by id", id);
        MediaUsageReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MediaUsageReadDto> create(@Valid @RequestBody MediaUsageCreateDto createDto) {
        log.info("POST /api/v1/mediausages - Creating new MediaUsage: {}", createDto);
        MediaUsageReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaUsageReadDto> update(@PathVariable Long id, @Valid @RequestBody MediaUsageUpdateDto updateDto) {
        log.info("PUT /api/v1/mediausages/{} - Updating MediaUsage: {}", id, updateDto);
        MediaUsageReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/mediausages/{} - Deleting MediaUsage", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MediaUsageReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/mediausages/{}/suspend - Suspending MediaUsage", id);
        MediaUsageReadDto suspended = service.suspendMediaUsage(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MediaUsageReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/mediausages/{}/activate - Activating MediaUsage", id);
        MediaUsageReadDto activated = service.activateMediaUsage(id);
        return ResponseEntity.ok(activated);
    }

}
