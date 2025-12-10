package com.example.blog.controller;

import com.example.blog.dto.CacheCreateDto;
import com.example.blog.dto.CacheReadDto;
import com.example.blog.dto.CacheUpdateDto;
import com.example.blog.service.CacheService;
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
@RequestMapping("/api/v1/caches")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final CacheService service;

    @GetMapping
    public ResponseEntity<Page<CacheReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/caches - Finding all Caches");
        Page<CacheReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CacheReadDto>> findAllList() {
        log.info("GET /api/v1/caches/all - Finding all Caches as list");
        List<CacheReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CacheReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/caches/{} - Finding Cache by id", id);
        CacheReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CacheReadDto> create(@Valid @RequestBody CacheCreateDto createDto) {
        log.info("POST /api/v1/caches - Creating new Cache: {}", createDto);
        CacheReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CacheReadDto> update(@PathVariable Long id, @Valid @RequestBody CacheUpdateDto updateDto) {
        log.info("PUT /api/v1/caches/{} - Updating Cache: {}", id, updateDto);
        CacheReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/caches/{} - Deleting Cache", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CacheReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/caches/{}/suspend - Suspending Cache", id);
        CacheReadDto suspended = service.suspendCache(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CacheReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/caches/{}/activate - Activating Cache", id);
        CacheReadDto activated = service.activateCache(id);
        return ResponseEntity.ok(activated);
    }

}
