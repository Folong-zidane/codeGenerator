package com.example.blog.controller;

import com.example.blog.dto.CDNConfigCreateDto;
import com.example.blog.dto.CDNConfigReadDto;
import com.example.blog.dto.CDNConfigUpdateDto;
import com.example.blog.service.CDNConfigService;
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
@RequestMapping("/api/v1/cdnconfigs")
@RequiredArgsConstructor
@Slf4j
public class CDNConfigController {

    private final CDNConfigService service;

    @GetMapping
    public ResponseEntity<Page<CDNConfigReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/cdnconfigs - Finding all CDNConfigs");
        Page<CDNConfigReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CDNConfigReadDto>> findAllList() {
        log.info("GET /api/v1/cdnconfigs/all - Finding all CDNConfigs as list");
        List<CDNConfigReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CDNConfigReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/cdnconfigs/{} - Finding CDNConfig by id", id);
        CDNConfigReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CDNConfigReadDto> create(@Valid @RequestBody CDNConfigCreateDto createDto) {
        log.info("POST /api/v1/cdnconfigs - Creating new CDNConfig: {}", createDto);
        CDNConfigReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CDNConfigReadDto> update(@PathVariable Long id, @Valid @RequestBody CDNConfigUpdateDto updateDto) {
        log.info("PUT /api/v1/cdnconfigs/{} - Updating CDNConfig: {}", id, updateDto);
        CDNConfigReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/cdnconfigs/{} - Deleting CDNConfig", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CDNConfigReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/cdnconfigs/{}/suspend - Suspending CDNConfig", id);
        CDNConfigReadDto suspended = service.suspendCDNConfig(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CDNConfigReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/cdnconfigs/{}/activate - Activating CDNConfig", id);
        CDNConfigReadDto activated = service.activateCDNConfig(id);
        return ResponseEntity.ok(activated);
    }

}
