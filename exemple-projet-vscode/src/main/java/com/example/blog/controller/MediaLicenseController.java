package com.example.blog.controller;

import com.example.blog.dto.MediaLicenseCreateDto;
import com.example.blog.dto.MediaLicenseReadDto;
import com.example.blog.dto.MediaLicenseUpdateDto;
import com.example.blog.service.MediaLicenseService;
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
@RequestMapping("/api/v1/medialicenses")
@RequiredArgsConstructor
@Slf4j
public class MediaLicenseController {

    private final MediaLicenseService service;

    @GetMapping
    public ResponseEntity<Page<MediaLicenseReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/medialicenses - Finding all MediaLicenses");
        Page<MediaLicenseReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaLicenseReadDto>> findAllList() {
        log.info("GET /api/v1/medialicenses/all - Finding all MediaLicenses as list");
        List<MediaLicenseReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaLicenseReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/medialicenses/{} - Finding MediaLicense by id", id);
        MediaLicenseReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MediaLicenseReadDto> create(@Valid @RequestBody MediaLicenseCreateDto createDto) {
        log.info("POST /api/v1/medialicenses - Creating new MediaLicense: {}", createDto);
        MediaLicenseReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaLicenseReadDto> update(@PathVariable Long id, @Valid @RequestBody MediaLicenseUpdateDto updateDto) {
        log.info("PUT /api/v1/medialicenses/{} - Updating MediaLicense: {}", id, updateDto);
        MediaLicenseReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/medialicenses/{} - Deleting MediaLicense", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MediaLicenseReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/medialicenses/{}/suspend - Suspending MediaLicense", id);
        MediaLicenseReadDto suspended = service.suspendMediaLicense(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MediaLicenseReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/medialicenses/{}/activate - Activating MediaLicense", id);
        MediaLicenseReadDto activated = service.activateMediaLicense(id);
        return ResponseEntity.ok(activated);
    }

}
