package com.example.blog.controller;

import com.example.blog.dto.PWAConfigCreateDto;
import com.example.blog.dto.PWAConfigReadDto;
import com.example.blog.dto.PWAConfigUpdateDto;
import com.example.blog.service.PWAConfigService;
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
@RequestMapping("/api/v1/pwaconfigs")
@RequiredArgsConstructor
@Slf4j
public class PWAConfigController {

    private final PWAConfigService service;

    @GetMapping
    public ResponseEntity<Page<PWAConfigReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/pwaconfigs - Finding all PWAConfigs");
        Page<PWAConfigReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PWAConfigReadDto>> findAllList() {
        log.info("GET /api/v1/pwaconfigs/all - Finding all PWAConfigs as list");
        List<PWAConfigReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PWAConfigReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/pwaconfigs/{} - Finding PWAConfig by id", id);
        PWAConfigReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PWAConfigReadDto> create(@Valid @RequestBody PWAConfigCreateDto createDto) {
        log.info("POST /api/v1/pwaconfigs - Creating new PWAConfig: {}", createDto);
        PWAConfigReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PWAConfigReadDto> update(@PathVariable Long id, @Valid @RequestBody PWAConfigUpdateDto updateDto) {
        log.info("PUT /api/v1/pwaconfigs/{} - Updating PWAConfig: {}", id, updateDto);
        PWAConfigReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/pwaconfigs/{} - Deleting PWAConfig", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PWAConfigReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/pwaconfigs/{}/suspend - Suspending PWAConfig", id);
        PWAConfigReadDto suspended = service.suspendPWAConfig(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PWAConfigReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/pwaconfigs/{}/activate - Activating PWAConfig", id);
        PWAConfigReadDto activated = service.activatePWAConfig(id);
        return ResponseEntity.ok(activated);
    }

}
