package com.example.blog.controller;

import com.example.blog.dto.RecoveryPointCreateDto;
import com.example.blog.dto.RecoveryPointReadDto;
import com.example.blog.dto.RecoveryPointUpdateDto;
import com.example.blog.service.RecoveryPointService;
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
@RequestMapping("/api/v1/recoverypoints")
@RequiredArgsConstructor
@Slf4j
public class RecoveryPointController {

    private final RecoveryPointService service;

    @GetMapping
    public ResponseEntity<Page<RecoveryPointReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/recoverypoints - Finding all RecoveryPoints");
        Page<RecoveryPointReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecoveryPointReadDto>> findAllList() {
        log.info("GET /api/v1/recoverypoints/all - Finding all RecoveryPoints as list");
        List<RecoveryPointReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecoveryPointReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/recoverypoints/{} - Finding RecoveryPoint by id", id);
        RecoveryPointReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RecoveryPointReadDto> create(@Valid @RequestBody RecoveryPointCreateDto createDto) {
        log.info("POST /api/v1/recoverypoints - Creating new RecoveryPoint: {}", createDto);
        RecoveryPointReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecoveryPointReadDto> update(@PathVariable Long id, @Valid @RequestBody RecoveryPointUpdateDto updateDto) {
        log.info("PUT /api/v1/recoverypoints/{} - Updating RecoveryPoint: {}", id, updateDto);
        RecoveryPointReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/recoverypoints/{} - Deleting RecoveryPoint", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<RecoveryPointReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/recoverypoints/{}/suspend - Suspending RecoveryPoint", id);
        RecoveryPointReadDto suspended = service.suspendRecoveryPoint(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RecoveryPointReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/recoverypoints/{}/activate - Activating RecoveryPoint", id);
        RecoveryPointReadDto activated = service.activateRecoveryPoint(id);
        return ResponseEntity.ok(activated);
    }

}
