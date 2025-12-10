package com.example.blog.controller;

import com.example.blog.dto.MediaFileCreateDto;
import com.example.blog.dto.MediaFileReadDto;
import com.example.blog.dto.MediaFileUpdateDto;
import com.example.blog.service.MediaFileService;
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
@RequestMapping("/api/v1/mediafiles")
@RequiredArgsConstructor
@Slf4j
public class MediaFileController {

    private final MediaFileService service;

    @GetMapping
    public ResponseEntity<Page<MediaFileReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/mediafiles - Finding all MediaFiles");
        Page<MediaFileReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaFileReadDto>> findAllList() {
        log.info("GET /api/v1/mediafiles/all - Finding all MediaFiles as list");
        List<MediaFileReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaFileReadDto> findById(@PathVariable UUID id) {
        log.info("GET /api/v1/mediafiles/{} - Finding MediaFile by id", id);
        MediaFileReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MediaFileReadDto> create(@Valid @RequestBody MediaFileCreateDto createDto) {
        log.info("POST /api/v1/mediafiles - Creating new MediaFile: {}", createDto);
        MediaFileReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaFileReadDto> update(@PathVariable UUID id, @Valid @RequestBody MediaFileUpdateDto updateDto) {
        log.info("PUT /api/v1/mediafiles/{} - Updating MediaFile: {}", id, updateDto);
        MediaFileReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /api/v1/mediafiles/{} - Deleting MediaFile", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MediaFileReadDto> suspend(@PathVariable UUID id) {
        log.info("PATCH /api/v1/mediafiles/{}/suspend - Suspending MediaFile", id);
        MediaFileReadDto suspended = service.suspendMediaFile(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MediaFileReadDto> activate(@PathVariable UUID id) {
        log.info("PATCH /api/v1/mediafiles/{}/activate - Activating MediaFile", id);
        MediaFileReadDto activated = service.activateMediaFile(id);
        return ResponseEntity.ok(activated);
    }

}
