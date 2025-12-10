package com.example.blog.controller;

import com.example.blog.dto.PaiementCreateDto;
import com.example.blog.dto.PaiementReadDto;
import com.example.blog.dto.PaiementUpdateDto;
import com.example.blog.service.PaiementService;
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
@RequestMapping("/api/v1/paiements")
@RequiredArgsConstructor
@Slf4j
public class PaiementController {

    private final PaiementService service;

    @GetMapping
    public ResponseEntity<Page<PaiementReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/paiements - Finding all Paiements");
        Page<PaiementReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaiementReadDto>> findAllList() {
        log.info("GET /api/v1/paiements/all - Finding all Paiements as list");
        List<PaiementReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/paiements/{} - Finding Paiement by id", id);
        PaiementReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaiementReadDto> create(@Valid @RequestBody PaiementCreateDto createDto) {
        log.info("POST /api/v1/paiements - Creating new Paiement: {}", createDto);
        PaiementReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaiementReadDto> update(@PathVariable Long id, @Valid @RequestBody PaiementUpdateDto updateDto) {
        log.info("PUT /api/v1/paiements/{} - Updating Paiement: {}", id, updateDto);
        PaiementReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/paiements/{} - Deleting Paiement", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PaiementReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/paiements/{}/suspend - Suspending Paiement", id);
        PaiementReadDto suspended = service.suspendPaiement(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PaiementReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/paiements/{}/activate - Activating Paiement", id);
        PaiementReadDto activated = service.activatePaiement(id);
        return ResponseEntity.ok(activated);
    }

}
