package com.example.blog.controller;

import com.example.blog.dto.ProduitPremiumCreateDto;
import com.example.blog.dto.ProduitPremiumReadDto;
import com.example.blog.dto.ProduitPremiumUpdateDto;
import com.example.blog.service.ProduitPremiumService;
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
@RequestMapping("/api/v1/produitpremiums")
@RequiredArgsConstructor
@Slf4j
public class ProduitPremiumController {

    private final ProduitPremiumService service;

    @GetMapping
    public ResponseEntity<Page<ProduitPremiumReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/produitpremiums - Finding all ProduitPremiums");
        Page<ProduitPremiumReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProduitPremiumReadDto>> findAllList() {
        log.info("GET /api/v1/produitpremiums/all - Finding all ProduitPremiums as list");
        List<ProduitPremiumReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitPremiumReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/produitpremiums/{} - Finding ProduitPremium by id", id);
        ProduitPremiumReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProduitPremiumReadDto> create(@Valid @RequestBody ProduitPremiumCreateDto createDto) {
        log.info("POST /api/v1/produitpremiums - Creating new ProduitPremium: {}", createDto);
        ProduitPremiumReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitPremiumReadDto> update(@PathVariable Long id, @Valid @RequestBody ProduitPremiumUpdateDto updateDto) {
        log.info("PUT /api/v1/produitpremiums/{} - Updating ProduitPremium: {}", id, updateDto);
        ProduitPremiumReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/produitpremiums/{} - Deleting ProduitPremium", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ProduitPremiumReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/produitpremiums/{}/suspend - Suspending ProduitPremium", id);
        ProduitPremiumReadDto suspended = service.suspendProduitPremium(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProduitPremiumReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/produitpremiums/{}/activate - Activating ProduitPremium", id);
        ProduitPremiumReadDto activated = service.activateProduitPremium(id);
        return ResponseEntity.ok(activated);
    }

}
