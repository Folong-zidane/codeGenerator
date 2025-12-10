package com.example.blog.controller;

import com.example.blog.dto.LangueCreateDto;
import com.example.blog.dto.LangueReadDto;
import com.example.blog.dto.LangueUpdateDto;
import com.example.blog.service.LangueService;
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
@RequestMapping("/api/v1/langues")
@RequiredArgsConstructor
@Slf4j
public class LangueController {

    private final LangueService service;

    @GetMapping
    public ResponseEntity<Page<LangueReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/langues - Finding all Langues");
        Page<LangueReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LangueReadDto>> findAllList() {
        log.info("GET /api/v1/langues/all - Finding all Langues as list");
        List<LangueReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LangueReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/langues/{} - Finding Langue by id", id);
        LangueReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LangueReadDto> create(@Valid @RequestBody LangueCreateDto createDto) {
        log.info("POST /api/v1/langues - Creating new Langue: {}", createDto);
        LangueReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LangueReadDto> update(@PathVariable Long id, @Valid @RequestBody LangueUpdateDto updateDto) {
        log.info("PUT /api/v1/langues/{} - Updating Langue: {}", id, updateDto);
        LangueReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/langues/{} - Deleting Langue", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<LangueReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/langues/{}/suspend - Suspending Langue", id);
        LangueReadDto suspended = service.suspendLangue(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LangueReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/langues/{}/activate - Activating Langue", id);
        LangueReadDto activated = service.activateLangue(id);
        return ResponseEntity.ok(activated);
    }

}
