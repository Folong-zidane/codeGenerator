package com.example.blog.controller;

import com.example.blog.dto.BackupCreateDto;
import com.example.blog.dto.BackupReadDto;
import com.example.blog.dto.BackupUpdateDto;
import com.example.blog.service.BackupService;
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
@RequestMapping("/api/v1/backups")
@RequiredArgsConstructor
@Slf4j
public class BackupController {

    private final BackupService service;

    @GetMapping
    public ResponseEntity<Page<BackupReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/backups - Finding all Backups");
        Page<BackupReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BackupReadDto>> findAllList() {
        log.info("GET /api/v1/backups/all - Finding all Backups as list");
        List<BackupReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BackupReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/backups/{} - Finding Backup by id", id);
        BackupReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BackupReadDto> create(@Valid @RequestBody BackupCreateDto createDto) {
        log.info("POST /api/v1/backups - Creating new Backup: {}", createDto);
        BackupReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BackupReadDto> update(@PathVariable Long id, @Valid @RequestBody BackupUpdateDto updateDto) {
        log.info("PUT /api/v1/backups/{} - Updating Backup: {}", id, updateDto);
        BackupReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/backups/{} - Deleting Backup", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<BackupReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/backups/{}/suspend - Suspending Backup", id);
        BackupReadDto suspended = service.suspendBackup(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<BackupReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/backups/{}/activate - Activating Backup", id);
        BackupReadDto activated = service.activateBackup(id);
        return ResponseEntity.ok(activated);
    }

}
