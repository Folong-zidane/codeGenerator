package com.example.blog.controller;

import com.example.blog.dto.LienPartageCreateDto;
import com.example.blog.dto.LienPartageReadDto;
import com.example.blog.dto.LienPartageUpdateDto;
import com.example.blog.service.LienPartageService;
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
@RequestMapping("/api/v1/lienpartages")
@RequiredArgsConstructor
@Slf4j
public class LienPartageController {

    private final LienPartageService service;

    @GetMapping
    public ResponseEntity<Page<LienPartageReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/lienpartages - Finding all LienPartages");
        Page<LienPartageReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LienPartageReadDto>> findAllList() {
        log.info("GET /api/v1/lienpartages/all - Finding all LienPartages as list");
        List<LienPartageReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LienPartageReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/lienpartages/{} - Finding LienPartage by id", id);
        LienPartageReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LienPartageReadDto> create(@Valid @RequestBody LienPartageCreateDto createDto) {
        log.info("POST /api/v1/lienpartages - Creating new LienPartage: {}", createDto);
        LienPartageReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LienPartageReadDto> update(@PathVariable Long id, @Valid @RequestBody LienPartageUpdateDto updateDto) {
        log.info("PUT /api/v1/lienpartages/{} - Updating LienPartage: {}", id, updateDto);
        LienPartageReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/lienpartages/{} - Deleting LienPartage", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<LienPartageReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/lienpartages/{}/suspend - Suspending LienPartage", id);
        LienPartageReadDto suspended = service.suspendLienPartage(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LienPartageReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/lienpartages/{}/activate - Activating LienPartage", id);
        LienPartageReadDto activated = service.activateLienPartage(id);
        return ResponseEntity.ok(activated);
    }

}
