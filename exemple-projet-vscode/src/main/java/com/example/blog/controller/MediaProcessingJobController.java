package com.example.blog.controller;

import com.example.blog.dto.MediaProcessingJobCreateDto;
import com.example.blog.dto.MediaProcessingJobReadDto;
import com.example.blog.dto.MediaProcessingJobUpdateDto;
import com.example.blog.service.MediaProcessingJobService;
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
@RequestMapping("/api/v1/mediaprocessingjobs")
@RequiredArgsConstructor
@Slf4j
public class MediaProcessingJobController {

    private final MediaProcessingJobService service;

    @GetMapping
    public ResponseEntity<Page<MediaProcessingJobReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/mediaprocessingjobs - Finding all MediaProcessingJobs");
        Page<MediaProcessingJobReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MediaProcessingJobReadDto>> findAllList() {
        log.info("GET /api/v1/mediaprocessingjobs/all - Finding all MediaProcessingJobs as list");
        List<MediaProcessingJobReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaProcessingJobReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/mediaprocessingjobs/{} - Finding MediaProcessingJob by id", id);
        MediaProcessingJobReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MediaProcessingJobReadDto> create(@Valid @RequestBody MediaProcessingJobCreateDto createDto) {
        log.info("POST /api/v1/mediaprocessingjobs - Creating new MediaProcessingJob: {}", createDto);
        MediaProcessingJobReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaProcessingJobReadDto> update(@PathVariable Long id, @Valid @RequestBody MediaProcessingJobUpdateDto updateDto) {
        log.info("PUT /api/v1/mediaprocessingjobs/{} - Updating MediaProcessingJob: {}", id, updateDto);
        MediaProcessingJobReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/mediaprocessingjobs/{} - Deleting MediaProcessingJob", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MediaProcessingJobReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/mediaprocessingjobs/{}/suspend - Suspending MediaProcessingJob", id);
        MediaProcessingJobReadDto suspended = service.suspendMediaProcessingJob(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MediaProcessingJobReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/mediaprocessingjobs/{}/activate - Activating MediaProcessingJob", id);
        MediaProcessingJobReadDto activated = service.activateMediaProcessingJob(id);
        return ResponseEntity.ok(activated);
    }

}
