package com.example.blog.controller;

import com.example.blog.dto.OfflineQueueCreateDto;
import com.example.blog.dto.OfflineQueueReadDto;
import com.example.blog.dto.OfflineQueueUpdateDto;
import com.example.blog.service.OfflineQueueService;
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
@RequestMapping("/api/v1/offlinequeues")
@RequiredArgsConstructor
@Slf4j
public class OfflineQueueController {

    private final OfflineQueueService service;

    @GetMapping
    public ResponseEntity<Page<OfflineQueueReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/offlinequeues - Finding all OfflineQueues");
        Page<OfflineQueueReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfflineQueueReadDto>> findAllList() {
        log.info("GET /api/v1/offlinequeues/all - Finding all OfflineQueues as list");
        List<OfflineQueueReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfflineQueueReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/offlinequeues/{} - Finding OfflineQueue by id", id);
        OfflineQueueReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OfflineQueueReadDto> create(@Valid @RequestBody OfflineQueueCreateDto createDto) {
        log.info("POST /api/v1/offlinequeues - Creating new OfflineQueue: {}", createDto);
        OfflineQueueReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfflineQueueReadDto> update(@PathVariable Long id, @Valid @RequestBody OfflineQueueUpdateDto updateDto) {
        log.info("PUT /api/v1/offlinequeues/{} - Updating OfflineQueue: {}", id, updateDto);
        OfflineQueueReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/offlinequeues/{} - Deleting OfflineQueue", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<OfflineQueueReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/offlinequeues/{}/suspend - Suspending OfflineQueue", id);
        OfflineQueueReadDto suspended = service.suspendOfflineQueue(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<OfflineQueueReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/offlinequeues/{}/activate - Activating OfflineQueue", id);
        OfflineQueueReadDto activated = service.activateOfflineQueue(id);
        return ResponseEntity.ok(activated);
    }

}
