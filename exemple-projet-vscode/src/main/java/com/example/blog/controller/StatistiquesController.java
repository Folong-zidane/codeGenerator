package com.example.blog.controller;

import com.example.blog.dto.StatistiquesCreateDto;
import com.example.blog.dto.StatistiquesReadDto;
import com.example.blog.dto.StatistiquesUpdateDto;
import com.example.blog.service.StatistiquesService;
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
@RequestMapping("/api/v1/statistiquess")
@RequiredArgsConstructor
@Slf4j
public class StatistiquesController {

    private final StatistiquesService service;

    @GetMapping
    public ResponseEntity<Page<StatistiquesReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/statistiquess - Finding all Statistiquess");
        Page<StatistiquesReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StatistiquesReadDto>> findAllList() {
        log.info("GET /api/v1/statistiquess/all - Finding all Statistiquess as list");
        List<StatistiquesReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatistiquesReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/statistiquess/{} - Finding Statistiques by id", id);
        StatistiquesReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<StatistiquesReadDto> create(@Valid @RequestBody StatistiquesCreateDto createDto) {
        log.info("POST /api/v1/statistiquess - Creating new Statistiques: {}", createDto);
        StatistiquesReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatistiquesReadDto> update(@PathVariable Long id, @Valid @RequestBody StatistiquesUpdateDto updateDto) {
        log.info("PUT /api/v1/statistiquess/{} - Updating Statistiques: {}", id, updateDto);
        StatistiquesReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/statistiquess/{} - Deleting Statistiques", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<StatistiquesReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/statistiquess/{}/suspend - Suspending Statistiques", id);
        StatistiquesReadDto suspended = service.suspendStatistiques(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StatistiquesReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/statistiquess/{}/activate - Activating Statistiques", id);
        StatistiquesReadDto activated = service.activateStatistiques(id);
        return ResponseEntity.ok(activated);
    }

}
