package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.EntityCreateDto;
import com.ecommerce.complex.dto.EntityReadDto;
import com.ecommerce.complex.dto.EntityUpdateDto;
import com.ecommerce.complex.service.EntityService;
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
@RequestMapping("/api/v1/entitys")
@RequiredArgsConstructor
@Slf4j
public class EntityController {

    private final EntityService service;

    @GetMapping
    public ResponseEntity<Page<EntityReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/entitys - Finding all Entitys");
        Page<EntityReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EntityReadDto>> findAllList() {
        log.info("GET /api/v1/entitys/all - Finding all Entitys as list");
        List<EntityReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/entitys/{} - Finding Entity by id", id);
        EntityReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EntityReadDto> create(@Valid @RequestBody EntityCreateDto createDto) {
        log.info("POST /api/v1/entitys - Creating new Entity: {}", createDto);
        EntityReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityReadDto> update(@PathVariable Long id, @Valid @RequestBody EntityUpdateDto updateDto) {
        log.info("PUT /api/v1/entitys/{} - Updating Entity: {}", id, updateDto);
        EntityReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/entitys/{} - Deleting Entity", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<EntityReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/entitys/{}/suspend - Suspending Entity", id);
        EntityReadDto suspended = service.suspendEntity(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EntityReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/entitys/{}/activate - Activating Entity", id);
        EntityReadDto activated = service.activateEntity(id);
        return ResponseEntity.ok(activated);
    }

}
