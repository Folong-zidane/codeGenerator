package com.example.blog.controller;

import com.example.blog.dto.ArticleTagCreateDto;
import com.example.blog.dto.ArticleTagReadDto;
import com.example.blog.dto.ArticleTagUpdateDto;
import com.example.blog.service.ArticleTagService;
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
@RequestMapping("/api/v1/articletags")
@RequiredArgsConstructor
@Slf4j
public class ArticleTagController {

    private final ArticleTagService service;

    @GetMapping
    public ResponseEntity<Page<ArticleTagReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/articletags - Finding all ArticleTags");
        Page<ArticleTagReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArticleTagReadDto>> findAllList() {
        log.info("GET /api/v1/articletags/all - Finding all ArticleTags as list");
        List<ArticleTagReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleTagReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/articletags/{} - Finding ArticleTag by id", id);
        ArticleTagReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ArticleTagReadDto> create(@Valid @RequestBody ArticleTagCreateDto createDto) {
        log.info("POST /api/v1/articletags - Creating new ArticleTag: {}", createDto);
        ArticleTagReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleTagReadDto> update(@PathVariable Long id, @Valid @RequestBody ArticleTagUpdateDto updateDto) {
        log.info("PUT /api/v1/articletags/{} - Updating ArticleTag: {}", id, updateDto);
        ArticleTagReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/articletags/{} - Deleting ArticleTag", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ArticleTagReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/articletags/{}/suspend - Suspending ArticleTag", id);
        ArticleTagReadDto suspended = service.suspendArticleTag(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ArticleTagReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/articletags/{}/activate - Activating ArticleTag", id);
        ArticleTagReadDto activated = service.activateArticleTag(id);
        return ResponseEntity.ok(activated);
    }

}
