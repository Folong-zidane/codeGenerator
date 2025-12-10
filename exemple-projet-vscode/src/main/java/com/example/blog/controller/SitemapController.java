package com.example.blog.controller;

import com.example.blog.dto.SitemapCreateDto;
import com.example.blog.dto.SitemapReadDto;
import com.example.blog.dto.SitemapUpdateDto;
import com.example.blog.service.SitemapService;
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
@RequestMapping("/api/v1/sitemaps")
@RequiredArgsConstructor
@Slf4j
public class SitemapController {

    private final SitemapService service;

    @GetMapping
    public ResponseEntity<Page<SitemapReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/sitemaps - Finding all Sitemaps");
        Page<SitemapReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SitemapReadDto>> findAllList() {
        log.info("GET /api/v1/sitemaps/all - Finding all Sitemaps as list");
        List<SitemapReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SitemapReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/sitemaps/{} - Finding Sitemap by id", id);
        SitemapReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SitemapReadDto> create(@Valid @RequestBody SitemapCreateDto createDto) {
        log.info("POST /api/v1/sitemaps - Creating new Sitemap: {}", createDto);
        SitemapReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SitemapReadDto> update(@PathVariable Long id, @Valid @RequestBody SitemapUpdateDto updateDto) {
        log.info("PUT /api/v1/sitemaps/{} - Updating Sitemap: {}", id, updateDto);
        SitemapReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/sitemaps/{} - Deleting Sitemap", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<SitemapReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/sitemaps/{}/suspend - Suspending Sitemap", id);
        SitemapReadDto suspended = service.suspendSitemap(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SitemapReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/sitemaps/{}/activate - Activating Sitemap", id);
        SitemapReadDto activated = service.activateSitemap(id);
        return ResponseEntity.ok(activated);
    }

}
