package com.example.blog.controller;

import com.example.blog.dto.FavoriCreateDto;
import com.example.blog.dto.FavoriReadDto;
import com.example.blog.dto.FavoriUpdateDto;
import com.example.blog.service.FavoriService;
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
@RequestMapping("/api/v1/favoris")
@RequiredArgsConstructor
@Slf4j
public class FavoriController {

    private final FavoriService service;

    @GetMapping
    public ResponseEntity<Page<FavoriReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/favoris - Finding all Favoris");
        Page<FavoriReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FavoriReadDto>> findAllList() {
        log.info("GET /api/v1/favoris/all - Finding all Favoris as list");
        List<FavoriReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/favoris/{} - Finding Favori by id", id);
        FavoriReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<FavoriReadDto> create(@Valid @RequestBody FavoriCreateDto createDto) {
        log.info("POST /api/v1/favoris - Creating new Favori: {}", createDto);
        FavoriReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriReadDto> update(@PathVariable Long id, @Valid @RequestBody FavoriUpdateDto updateDto) {
        log.info("PUT /api/v1/favoris/{} - Updating Favori: {}", id, updateDto);
        FavoriReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/favoris/{} - Deleting Favori", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<FavoriReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/favoris/{}/suspend - Suspending Favori", id);
        FavoriReadDto suspended = service.suspendFavori(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<FavoriReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/favoris/{}/activate - Activating Favori", id);
        FavoriReadDto activated = service.activateFavori(id);
        return ResponseEntity.ok(activated);
    }

}
