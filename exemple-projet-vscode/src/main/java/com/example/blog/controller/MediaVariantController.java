package com.example.blog.controller;

import com.example.blog.dto.MediaVariantCreateDto;
import com.example.blog.dto.MediaVariantReadDto;
import com.example.blog.dto.MediaVariantUpdateDto;
import com.example.blog.service.MediaVariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mediavariants")
@RequiredArgsConstructor
@Slf4j
public class MediaVariantController {

    private final MediaVariantService service;

    @GetMapping
    public ResponseEntity<Page<MediaVariantReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/mediavariants - Finding all MediaVariants");
        Page<MediaVariantReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaVariantReadDto>> findAllList() {
        log.info("GET /api/v1/mediavariants/all - Finding all MediaVariants as list");
        List<MediaVariantReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaVariantReadDto> findById(@PathVariable UUID id) {
        log.info("GET /api/v1/mediavariants/{} - Finding MediaVariant by id", id);
        MediaVariantReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MediaVariantReadDto> create(@Valid @RequestBody MediaVariantCreateDto createDto) {
        log.info("POST /api/v1/mediavariants - Creating new MediaVariant: {}", createDto);
        MediaVariantReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaVariantReadDto> update(@PathVariable UUID id, @Valid @RequestBody MediaVariantUpdateDto updateDto) {
        log.info("PUT /api/v1/mediavariants/{} - Updating MediaVariant: {}", id, updateDto);
        MediaVariantReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /api/v1/mediavariants/{} - Deleting MediaVariant", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MediaVariantReadDto> suspend(@PathVariable UUID id) {
        log.info("PATCH /api/v1/mediavariants/{}/suspend - Suspending MediaVariant", id);
        MediaVariantReadDto suspended = service.suspendMediaVariant(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MediaVariantReadDto> activate(@PathVariable UUID id) {
        log.info("PATCH /api/v1/mediavariants/{}/activate - Activating MediaVariant", id);
        MediaVariantReadDto activated = service.activateMediaVariant(id);
        return ResponseEntity.ok(activated);
    }

}
