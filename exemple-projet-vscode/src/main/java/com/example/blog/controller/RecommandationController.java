package com.example.blog.controller;

import com.example.blog.dto.RecommandationCreateDto;
import com.example.blog.dto.RecommandationReadDto;
import com.example.blog.dto.RecommandationUpdateDto;
import com.example.blog.service.RecommandationService;
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
@RequestMapping("/api/v1/recommandations")
@RequiredArgsConstructor
@Slf4j
public class RecommandationController {

    private final RecommandationService service;

    @GetMapping
    public ResponseEntity<Page<RecommandationReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/recommandations - Finding all Recommandations");
        Page<RecommandationReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecommandationReadDto>> findAllList() {
        log.info("GET /api/v1/recommandations/all - Finding all Recommandations as list");
        List<RecommandationReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommandationReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/recommandations/{} - Finding Recommandation by id", id);
        RecommandationReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RecommandationReadDto> create(@Valid @RequestBody RecommandationCreateDto createDto) {
        log.info("POST /api/v1/recommandations - Creating new Recommandation: {}", createDto);
        RecommandationReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecommandationReadDto> update(@PathVariable Long id, @Valid @RequestBody RecommandationUpdateDto updateDto) {
        log.info("PUT /api/v1/recommandations/{} - Updating Recommandation: {}", id, updateDto);
        RecommandationReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/recommandations/{} - Deleting Recommandation", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<RecommandationReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/recommandations/{}/suspend - Suspending Recommandation", id);
        RecommandationReadDto suspended = service.suspendRecommandation(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RecommandationReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/recommandations/{}/activate - Activating Recommandation", id);
        RecommandationReadDto activated = service.activateRecommandation(id);
        return ResponseEntity.ok(activated);
    }

}
