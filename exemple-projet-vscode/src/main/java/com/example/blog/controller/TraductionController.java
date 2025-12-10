package com.example.blog.controller;

import com.example.blog.dto.TraductionCreateDto;
import com.example.blog.dto.TraductionReadDto;
import com.example.blog.dto.TraductionUpdateDto;
import com.example.blog.service.TraductionService;
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
@RequestMapping("/api/v1/traductions")
@RequiredArgsConstructor
@Slf4j
public class TraductionController {

    private final TraductionService service;

    @GetMapping
    public ResponseEntity<Page<TraductionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/traductions - Finding all Traductions");
        Page<TraductionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TraductionReadDto>> findAllList() {
        log.info("GET /api/v1/traductions/all - Finding all Traductions as list");
        List<TraductionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraductionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/traductions/{} - Finding Traduction by id", id);
        TraductionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TraductionReadDto> create(@Valid @RequestBody TraductionCreateDto createDto) {
        log.info("POST /api/v1/traductions - Creating new Traduction: {}", createDto);
        TraductionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraductionReadDto> update(@PathVariable Long id, @Valid @RequestBody TraductionUpdateDto updateDto) {
        log.info("PUT /api/v1/traductions/{} - Updating Traduction: {}", id, updateDto);
        TraductionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/traductions/{} - Deleting Traduction", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<TraductionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/traductions/{}/suspend - Suspending Traduction", id);
        TraductionReadDto suspended = service.suspendTraduction(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TraductionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/traductions/{}/activate - Activating Traduction", id);
        TraductionReadDto activated = service.activateTraduction(id);
        return ResponseEntity.ok(activated);
    }

}
