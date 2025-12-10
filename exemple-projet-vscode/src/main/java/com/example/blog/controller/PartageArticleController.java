package com.example.blog.controller;

import com.example.blog.dto.PartageArticleCreateDto;
import com.example.blog.dto.PartageArticleReadDto;
import com.example.blog.dto.PartageArticleUpdateDto;
import com.example.blog.service.PartageArticleService;
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
@RequestMapping("/api/v1/partagearticles")
@RequiredArgsConstructor
@Slf4j
public class PartageArticleController {

    private final PartageArticleService service;

    @GetMapping
    public ResponseEntity<Page<PartageArticleReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/partagearticles - Finding all PartageArticles");
        Page<PartageArticleReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartageArticleReadDto>> findAllList() {
        log.info("GET /api/v1/partagearticles/all - Finding all PartageArticles as list");
        List<PartageArticleReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartageArticleReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/partagearticles/{} - Finding PartageArticle by id", id);
        PartageArticleReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PartageArticleReadDto> create(@Valid @RequestBody PartageArticleCreateDto createDto) {
        log.info("POST /api/v1/partagearticles - Creating new PartageArticle: {}", createDto);
        PartageArticleReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartageArticleReadDto> update(@PathVariable Long id, @Valid @RequestBody PartageArticleUpdateDto updateDto) {
        log.info("PUT /api/v1/partagearticles/{} - Updating PartageArticle: {}", id, updateDto);
        PartageArticleReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/partagearticles/{} - Deleting PartageArticle", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PartageArticleReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/partagearticles/{}/suspend - Suspending PartageArticle", id);
        PartageArticleReadDto suspended = service.suspendPartageArticle(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PartageArticleReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/partagearticles/{}/activate - Activating PartageArticle", id);
        PartageArticleReadDto activated = service.activatePartageArticle(id);
        return ResponseEntity.ok(activated);
    }

}
