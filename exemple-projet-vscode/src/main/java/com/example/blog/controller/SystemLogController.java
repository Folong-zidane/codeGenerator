package com.example.blog.controller;

import com.example.blog.dto.SystemLogCreateDto;
import com.example.blog.dto.SystemLogReadDto;
import com.example.blog.dto.SystemLogUpdateDto;
import com.example.blog.service.SystemLogService;
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
@RequestMapping("/api/v1/systemlogs")
@RequiredArgsConstructor
@Slf4j
public class SystemLogController {

    private final SystemLogService service;

    @GetMapping
    public ResponseEntity<Page<SystemLogReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/systemlogs - Finding all SystemLogs");
        Page<SystemLogReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SystemLogReadDto>> findAllList() {
        log.info("GET /api/v1/systemlogs/all - Finding all SystemLogs as list");
        List<SystemLogReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemLogReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/systemlogs/{} - Finding SystemLog by id", id);
        SystemLogReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SystemLogReadDto> create(@Valid @RequestBody SystemLogCreateDto createDto) {
        log.info("POST /api/v1/systemlogs - Creating new SystemLog: {}", createDto);
        SystemLogReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemLogReadDto> update(@PathVariable Long id, @Valid @RequestBody SystemLogUpdateDto updateDto) {
        log.info("PUT /api/v1/systemlogs/{} - Updating SystemLog: {}", id, updateDto);
        SystemLogReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/systemlogs/{} - Deleting SystemLog", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<SystemLogReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/systemlogs/{}/suspend - Suspending SystemLog", id);
        SystemLogReadDto suspended = service.suspendSystemLog(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SystemLogReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/systemlogs/{}/activate - Activating SystemLog", id);
        SystemLogReadDto activated = service.activateSystemLog(id);
        return ResponseEntity.ok(activated);
    }

}
