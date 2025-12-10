package com.example.blog.controller;

import com.example.blog.dto.SSOConfigCreateDto;
import com.example.blog.dto.SSOConfigReadDto;
import com.example.blog.dto.SSOConfigUpdateDto;
import com.example.blog.service.SSOConfigService;
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
@RequestMapping("/api/v1/ssoconfigs")
@RequiredArgsConstructor
@Slf4j
public class SSOConfigController {

    private final SSOConfigService service;

    @GetMapping
    public ResponseEntity<Page<SSOConfigReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/ssoconfigs - Finding all SSOConfigs");
        Page<SSOConfigReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SSOConfigReadDto>> findAllList() {
        log.info("GET /api/v1/ssoconfigs/all - Finding all SSOConfigs as list");
        List<SSOConfigReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SSOConfigReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/ssoconfigs/{} - Finding SSOConfig by id", id);
        SSOConfigReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SSOConfigReadDto> create(@Valid @RequestBody SSOConfigCreateDto createDto) {
        log.info("POST /api/v1/ssoconfigs - Creating new SSOConfig: {}", createDto);
        SSOConfigReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SSOConfigReadDto> update(@PathVariable Long id, @Valid @RequestBody SSOConfigUpdateDto updateDto) {
        log.info("PUT /api/v1/ssoconfigs/{} - Updating SSOConfig: {}", id, updateDto);
        SSOConfigReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/ssoconfigs/{} - Deleting SSOConfig", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<SSOConfigReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/ssoconfigs/{}/suspend - Suspending SSOConfig", id);
        SSOConfigReadDto suspended = service.suspendSSOConfig(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SSOConfigReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/ssoconfigs/{}/activate - Activating SSOConfig", id);
        SSOConfigReadDto activated = service.activateSSOConfig(id);
        return ResponseEntity.ok(activated);
    }

}
