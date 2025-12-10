package com.example.blog.controller;

import com.example.blog.dto.PanierCreateDto;
import com.example.blog.dto.PanierReadDto;
import com.example.blog.dto.PanierUpdateDto;
import com.example.blog.service.PanierService;
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
@RequestMapping("/api/v1/paniers")
@RequiredArgsConstructor
@Slf4j
public class PanierController {

    private final PanierService service;

    @GetMapping
    public ResponseEntity<Page<PanierReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/paniers - Finding all Paniers");
        Page<PanierReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PanierReadDto>> findAllList() {
        log.info("GET /api/v1/paniers/all - Finding all Paniers as list");
        List<PanierReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PanierReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/paniers/{} - Finding Panier by id", id);
        PanierReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PanierReadDto> create(@Valid @RequestBody PanierCreateDto createDto) {
        log.info("POST /api/v1/paniers - Creating new Panier: {}", createDto);
        PanierReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PanierReadDto> update(@PathVariable Long id, @Valid @RequestBody PanierUpdateDto updateDto) {
        log.info("PUT /api/v1/paniers/{} - Updating Panier: {}", id, updateDto);
        PanierReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/paniers/{} - Deleting Panier", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PanierReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/paniers/{}/suspend - Suspending Panier", id);
        PanierReadDto suspended = service.suspendPanier(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PanierReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/paniers/{}/activate - Activating Panier", id);
        PanierReadDto activated = service.activatePanier(id);
        return ResponseEntity.ok(activated);
    }

}
