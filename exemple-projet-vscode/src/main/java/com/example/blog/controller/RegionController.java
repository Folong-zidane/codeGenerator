package com.example.blog.controller;

import com.example.blog.dto.RegionCreateDto;
import com.example.blog.dto.RegionReadDto;
import com.example.blog.dto.RegionUpdateDto;
import com.example.blog.service.RegionService;
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
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
@Slf4j
public class RegionController {

    private final RegionService service;

    @GetMapping
    public ResponseEntity<Page<RegionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/regions - Finding all Regions");
        Page<RegionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegionReadDto>> findAllList() {
        log.info("GET /api/v1/regions/all - Finding all Regions as list");
        List<RegionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/regions/{} - Finding Region by id", id);
        RegionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RegionReadDto> create(@Valid @RequestBody RegionCreateDto createDto) {
        log.info("POST /api/v1/regions - Creating new Region: {}", createDto);
        RegionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionReadDto> update(@PathVariable Long id, @Valid @RequestBody RegionUpdateDto updateDto) {
        log.info("PUT /api/v1/regions/{} - Updating Region: {}", id, updateDto);
        RegionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/regions/{} - Deleting Region", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<RegionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/regions/{}/suspend - Suspending Region", id);
        RegionReadDto suspended = service.suspendRegion(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RegionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/regions/{}/activate - Activating Region", id);
        RegionReadDto activated = service.activateRegion(id);
        return ResponseEntity.ok(activated);
    }

}
