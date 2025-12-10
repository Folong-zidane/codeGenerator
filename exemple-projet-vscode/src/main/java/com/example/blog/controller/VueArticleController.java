package com.example.blog.controller;

import com.example.blog.dto.VueArticleCreateDto;
import com.example.blog.dto.VueArticleReadDto;
import com.example.blog.dto.VueArticleUpdateDto;
import com.example.blog.service.VueArticleService;
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
@RequestMapping("/api/v1/vuearticles")
@RequiredArgsConstructor
@Slf4j
public class VueArticleController {

    private final VueArticleService service;

    @GetMapping
    public ResponseEntity<Page<VueArticleReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/vuearticles - Finding all VueArticles");
        Page<VueArticleReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VueArticleReadDto>> findAllList() {
        log.info("GET /api/v1/vuearticles/all - Finding all VueArticles as list");
        List<VueArticleReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VueArticleReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/vuearticles/{} - Finding VueArticle by id", id);
        VueArticleReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VueArticleReadDto> create(@Valid @RequestBody VueArticleCreateDto createDto) {
        log.info("POST /api/v1/vuearticles - Creating new VueArticle: {}", createDto);
        VueArticleReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VueArticleReadDto> update(@PathVariable Long id, @Valid @RequestBody VueArticleUpdateDto updateDto) {
        log.info("PUT /api/v1/vuearticles/{} - Updating VueArticle: {}", id, updateDto);
        VueArticleReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/vuearticles/{} - Deleting VueArticle", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<VueArticleReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/vuearticles/{}/suspend - Suspending VueArticle", id);
        VueArticleReadDto suspended = service.suspendVueArticle(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<VueArticleReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/vuearticles/{}/activate - Activating VueArticle", id);
        VueArticleReadDto activated = service.activateVueArticle(id);
        return ResponseEntity.ok(activated);
    }

}
