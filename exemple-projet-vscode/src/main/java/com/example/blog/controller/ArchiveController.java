package com.example.blog.controller;

import com.example.blog.dto.ArchiveCreateDto;
import com.example.blog.dto.ArchiveReadDto;
import com.example.blog.dto.ArchiveUpdateDto;
import com.example.blog.service.ArchiveService;
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
@RequestMapping("/api/v1/archives")
@RequiredArgsConstructor
@Slf4j
public class ArchiveController {

    private final ArchiveService service;

    @GetMapping
    public ResponseEntity<Page<ArchiveReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/archives - Finding all Archives");
        Page<ArchiveReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArchiveReadDto>> findAllList() {
        log.info("GET /api/v1/archives/all - Finding all Archives as list");
        List<ArchiveReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchiveReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/archives/{} - Finding Archive by id", id);
        ArchiveReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ArchiveReadDto> create(@Valid @RequestBody ArchiveCreateDto createDto) {
        log.info("POST /api/v1/archives - Creating new Archive: {}", createDto);
        ArchiveReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArchiveReadDto> update(@PathVariable Long id, @Valid @RequestBody ArchiveUpdateDto updateDto) {
        log.info("PUT /api/v1/archives/{} - Updating Archive: {}", id, updateDto);
        ArchiveReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/archives/{} - Deleting Archive", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ArchiveReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/archives/{}/suspend - Suspending Archive", id);
        ArchiveReadDto suspended = service.suspendArchive(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ArchiveReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/archives/{}/activate - Activating Archive", id);
        ArchiveReadDto activated = service.activateArchive(id);
        return ResponseEntity.ok(activated);
    }

}
