package com.example.blog.controller;

import com.example.blog.dto.LangueContenuCreateDto;
import com.example.blog.dto.LangueContenuReadDto;
import com.example.blog.dto.LangueContenuUpdateDto;
import com.example.blog.service.LangueContenuService;
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
@RequestMapping("/api/v1/languecontenus")
@RequiredArgsConstructor
@Slf4j
public class LangueContenuController {

    private final LangueContenuService service;

    @GetMapping
    public ResponseEntity<Page<LangueContenuReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/languecontenus - Finding all LangueContenus");
        Page<LangueContenuReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LangueContenuReadDto>> findAllList() {
        log.info("GET /api/v1/languecontenus/all - Finding all LangueContenus as list");
        List<LangueContenuReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LangueContenuReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/languecontenus/{} - Finding LangueContenu by id", id);
        LangueContenuReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LangueContenuReadDto> create(@Valid @RequestBody LangueContenuCreateDto createDto) {
        log.info("POST /api/v1/languecontenus - Creating new LangueContenu: {}", createDto);
        LangueContenuReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LangueContenuReadDto> update(@PathVariable Long id, @Valid @RequestBody LangueContenuUpdateDto updateDto) {
        log.info("PUT /api/v1/languecontenus/{} - Updating LangueContenu: {}", id, updateDto);
        LangueContenuReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/languecontenus/{} - Deleting LangueContenu", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<LangueContenuReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/languecontenus/{}/suspend - Suspending LangueContenu", id);
        LangueContenuReadDto suspended = service.suspendLangueContenu(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<LangueContenuReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/languecontenus/{}/activate - Activating LangueContenu", id);
        LangueContenuReadDto activated = service.activateLangueContenu(id);
        return ResponseEntity.ok(activated);
    }

}
