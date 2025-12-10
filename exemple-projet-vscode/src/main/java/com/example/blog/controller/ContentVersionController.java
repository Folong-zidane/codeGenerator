package com.example.blog.controller;

import com.example.blog.dto.ContentVersionCreateDto;
import com.example.blog.dto.ContentVersionReadDto;
import com.example.blog.dto.ContentVersionUpdateDto;
import com.example.blog.service.ContentVersionService;
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
@RequestMapping("/api/v1/contentversions")
@RequiredArgsConstructor
@Slf4j
public class ContentVersionController {

    private final ContentVersionService service;

    @GetMapping
    public ResponseEntity<Page<ContentVersionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/contentversions - Finding all ContentVersions");
        Page<ContentVersionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContentVersionReadDto>> findAllList() {
        log.info("GET /api/v1/contentversions/all - Finding all ContentVersions as list");
        List<ContentVersionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentVersionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/contentversions/{} - Finding ContentVersion by id", id);
        ContentVersionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContentVersionReadDto> create(@Valid @RequestBody ContentVersionCreateDto createDto) {
        log.info("POST /api/v1/contentversions - Creating new ContentVersion: {}", createDto);
        ContentVersionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentVersionReadDto> update(@PathVariable Long id, @Valid @RequestBody ContentVersionUpdateDto updateDto) {
        log.info("PUT /api/v1/contentversions/{} - Updating ContentVersion: {}", id, updateDto);
        ContentVersionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/contentversions/{} - Deleting ContentVersion", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ContentVersionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/contentversions/{}/suspend - Suspending ContentVersion", id);
        ContentVersionReadDto suspended = service.suspendContentVersion(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ContentVersionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/contentversions/{}/activate - Activating ContentVersion", id);
        ContentVersionReadDto activated = service.activateContentVersion(id);
        return ResponseEntity.ok(activated);
    }

}
