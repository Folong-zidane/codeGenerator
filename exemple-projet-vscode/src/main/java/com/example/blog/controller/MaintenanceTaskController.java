package com.example.blog.controller;

import com.example.blog.dto.MaintenanceTaskCreateDto;
import com.example.blog.dto.MaintenanceTaskReadDto;
import com.example.blog.dto.MaintenanceTaskUpdateDto;
import com.example.blog.service.MaintenanceTaskService;
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
@RequestMapping("/api/v1/maintenancetasks")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceTaskController {

    private final MaintenanceTaskService service;

    @GetMapping
    public ResponseEntity<Page<MaintenanceTaskReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/maintenancetasks - Finding all MaintenanceTasks");
        Page<MaintenanceTaskReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceTaskReadDto>> findAllList() {
        log.info("GET /api/v1/maintenancetasks/all - Finding all MaintenanceTasks as list");
        List<MaintenanceTaskReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTaskReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/maintenancetasks/{} - Finding MaintenanceTask by id", id);
        MaintenanceTaskReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MaintenanceTaskReadDto> create(@Valid @RequestBody MaintenanceTaskCreateDto createDto) {
        log.info("POST /api/v1/maintenancetasks - Creating new MaintenanceTask: {}", createDto);
        MaintenanceTaskReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTaskReadDto> update(@PathVariable Long id, @Valid @RequestBody MaintenanceTaskUpdateDto updateDto) {
        log.info("PUT /api/v1/maintenancetasks/{} - Updating MaintenanceTask: {}", id, updateDto);
        MaintenanceTaskReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/maintenancetasks/{} - Deleting MaintenanceTask", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MaintenanceTaskReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/maintenancetasks/{}/suspend - Suspending MaintenanceTask", id);
        MaintenanceTaskReadDto suspended = service.suspendMaintenanceTask(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MaintenanceTaskReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/maintenancetasks/{}/activate - Activating MaintenanceTask", id);
        MaintenanceTaskReadDto activated = service.activateMaintenanceTask(id);
        return ResponseEntity.ok(activated);
    }

}
