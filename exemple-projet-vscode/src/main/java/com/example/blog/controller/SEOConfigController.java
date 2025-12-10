package com.example.blog.controller;

import com.example.blog.dto.SEOConfigCreateDto;
import com.example.blog.dto.SEOConfigReadDto;
import com.example.blog.dto.SEOConfigUpdateDto;
import com.example.blog.service.SEOConfigService;
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
@RequestMapping("/api/v1/seoconfigs")
@RequiredArgsConstructor
@Slf4j
public class SEOConfigController {

    private final SEOConfigService service;

    @GetMapping
    public ResponseEntity<Page<SEOConfigReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/seoconfigs - Finding all SEOConfigs");
        Page<SEOConfigReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SEOConfigReadDto>> findAllList() {
        log.info("GET /api/v1/seoconfigs/all - Finding all SEOConfigs as list");
        List<SEOConfigReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SEOConfigReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/seoconfigs/{} - Finding SEOConfig by id", id);
        SEOConfigReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SEOConfigReadDto> create(@Valid @RequestBody SEOConfigCreateDto createDto) {
        log.info("POST /api/v1/seoconfigs - Creating new SEOConfig: {}", createDto);
        SEOConfigReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SEOConfigReadDto> update(@PathVariable Long id, @Valid @RequestBody SEOConfigUpdateDto updateDto) {
        log.info("PUT /api/v1/seoconfigs/{} - Updating SEOConfig: {}", id, updateDto);
        SEOConfigReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/seoconfigs/{} - Deleting SEOConfig", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<SEOConfigReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/seoconfigs/{}/suspend - Suspending SEOConfig", id);
        SEOConfigReadDto suspended = service.suspendSEOConfig(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SEOConfigReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/seoconfigs/{}/activate - Activating SEOConfig", id);
        SEOConfigReadDto activated = service.activateSEOConfig(id);
        return ResponseEntity.ok(activated);
    }

}
