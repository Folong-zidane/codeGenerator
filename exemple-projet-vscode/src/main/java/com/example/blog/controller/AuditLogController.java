package com.example.blog.controller;

import com.example.blog.dto.AuditLogCreateDto;
import com.example.blog.dto.AuditLogReadDto;
import com.example.blog.dto.AuditLogUpdateDto;
import com.example.blog.service.AuditLogService;
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
@RequestMapping("/api/v1/auditlogs")
@RequiredArgsConstructor
@Slf4j
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping
    public ResponseEntity<Page<AuditLogReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/auditlogs - Finding all AuditLogs");
        Page<AuditLogReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuditLogReadDto>> findAllList() {
        log.info("GET /api/v1/auditlogs/all - Finding all AuditLogs as list");
        List<AuditLogReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/auditlogs/{} - Finding AuditLog by id", id);
        AuditLogReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AuditLogReadDto> create(@Valid @RequestBody AuditLogCreateDto createDto) {
        log.info("POST /api/v1/auditlogs - Creating new AuditLog: {}", createDto);
        AuditLogReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogReadDto> update(@PathVariable Long id, @Valid @RequestBody AuditLogUpdateDto updateDto) {
        log.info("PUT /api/v1/auditlogs/{} - Updating AuditLog: {}", id, updateDto);
        AuditLogReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/auditlogs/{} - Deleting AuditLog", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AuditLogReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/auditlogs/{}/suspend - Suspending AuditLog", id);
        AuditLogReadDto suspended = service.suspendAuditLog(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AuditLogReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/auditlogs/{}/activate - Activating AuditLog", id);
        AuditLogReadDto activated = service.activateAuditLog(id);
        return ResponseEntity.ok(activated);
    }

}
