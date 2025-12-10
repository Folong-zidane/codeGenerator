package com.example.blog.controller;

import com.example.blog.dto.UtilisateurCreateDto;
import com.example.blog.dto.UtilisateurReadDto;
import com.example.blog.dto.UtilisateurUpdateDto;
import com.example.blog.service.UtilisateurService;
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
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
@Slf4j
public class UtilisateurController {

    private final UtilisateurService service;

    @GetMapping
    public ResponseEntity<Page<UtilisateurReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/utilisateurs - Finding all Utilisateurs");
        Page<UtilisateurReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UtilisateurReadDto>> findAllList() {
        log.info("GET /api/v1/utilisateurs/all - Finding all Utilisateurs as list");
        List<UtilisateurReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/utilisateurs/{} - Finding Utilisateur by id", id);
        UtilisateurReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UtilisateurReadDto> create(@Valid @RequestBody UtilisateurCreateDto createDto) {
        log.info("POST /api/v1/utilisateurs - Creating new Utilisateur: {}", createDto);
        UtilisateurReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurReadDto> update(@PathVariable Long id, @Valid @RequestBody UtilisateurUpdateDto updateDto) {
        log.info("PUT /api/v1/utilisateurs/{} - Updating Utilisateur: {}", id, updateDto);
        UtilisateurReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/utilisateurs/{} - Deleting Utilisateur", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<UtilisateurReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/utilisateurs/{}/suspend - Suspending Utilisateur", id);
        UtilisateurReadDto suspended = service.suspendUtilisateur(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UtilisateurReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/utilisateurs/{}/activate - Activating Utilisateur", id);
        UtilisateurReadDto activated = service.activateUtilisateur(id);
        return ResponseEntity.ok(activated);
    }

}
