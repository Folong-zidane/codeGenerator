package com.example.blog.controller;

import com.example.blog.dto.TelechargementMediaCreateDto;
import com.example.blog.dto.TelechargementMediaReadDto;
import com.example.blog.dto.TelechargementMediaUpdateDto;
import com.example.blog.service.TelechargementMediaService;
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
@RequestMapping("/api/v1/telechargementmedias")
@RequiredArgsConstructor
@Slf4j
public class TelechargementMediaController {

    private final TelechargementMediaService service;

    @GetMapping
    public ResponseEntity<Page<TelechargementMediaReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/telechargementmedias - Finding all TelechargementMedias");
        Page<TelechargementMediaReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TelechargementMediaReadDto>> findAllList() {
        log.info("GET /api/v1/telechargementmedias/all - Finding all TelechargementMedias as list");
        List<TelechargementMediaReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TelechargementMediaReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/telechargementmedias/{} - Finding TelechargementMedia by id", id);
        TelechargementMediaReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TelechargementMediaReadDto> create(@Valid @RequestBody TelechargementMediaCreateDto createDto) {
        log.info("POST /api/v1/telechargementmedias - Creating new TelechargementMedia: {}", createDto);
        TelechargementMediaReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TelechargementMediaReadDto> update(@PathVariable Long id, @Valid @RequestBody TelechargementMediaUpdateDto updateDto) {
        log.info("PUT /api/v1/telechargementmedias/{} - Updating TelechargementMedia: {}", id, updateDto);
        TelechargementMediaReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/telechargementmedias/{} - Deleting TelechargementMedia", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<TelechargementMediaReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/telechargementmedias/{}/suspend - Suspending TelechargementMedia", id);
        TelechargementMediaReadDto suspended = service.suspendTelechargementMedia(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TelechargementMediaReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/telechargementmedias/{}/activate - Activating TelechargementMedia", id);
        TelechargementMediaReadDto activated = service.activateTelechargementMedia(id);
        return ResponseEntity.ok(activated);
    }

}
