package com.example.blog.controller;

import com.example.blog.dto.AdministrateurCreateDto;
import com.example.blog.dto.AdministrateurReadDto;
import com.example.blog.dto.AdministrateurUpdateDto;
import com.example.blog.service.AdministrateurService;
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
@RequestMapping("/api/v1/administrateurs")
@RequiredArgsConstructor
@Slf4j
public class AdministrateurController {

    private final AdministrateurService service;

    @GetMapping
    public ResponseEntity<Page<AdministrateurReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/administrateurs - Finding all Administrateurs");
        Page<AdministrateurReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdministrateurReadDto>> findAllList() {
        log.info("GET /api/v1/administrateurs/all - Finding all Administrateurs as list");
        List<AdministrateurReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/administrateurs/{} - Finding Administrateur by id", id);
        AdministrateurReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AdministrateurReadDto> create(@Valid @RequestBody AdministrateurCreateDto createDto) {
        log.info("POST /api/v1/administrateurs - Creating new Administrateur: {}", createDto);
        AdministrateurReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurReadDto> update(@PathVariable Long id, @Valid @RequestBody AdministrateurUpdateDto updateDto) {
        log.info("PUT /api/v1/administrateurs/{} - Updating Administrateur: {}", id, updateDto);
        AdministrateurReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/administrateurs/{} - Deleting Administrateur", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AdministrateurReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/administrateurs/{}/suspend - Suspending Administrateur", id);
        AdministrateurReadDto suspended = service.suspendAdministrateur(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AdministrateurReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/administrateurs/{}/activate - Activating Administrateur", id);
        AdministrateurReadDto activated = service.activateAdministrateur(id);
        return ResponseEntity.ok(activated);
    }

}
