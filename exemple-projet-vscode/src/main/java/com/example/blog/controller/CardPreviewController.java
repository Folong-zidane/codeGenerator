package com.example.blog.controller;

import com.example.blog.dto.CardPreviewCreateDto;
import com.example.blog.dto.CardPreviewReadDto;
import com.example.blog.dto.CardPreviewUpdateDto;
import com.example.blog.service.CardPreviewService;
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
@RequestMapping("/api/v1/cardpreviews")
@RequiredArgsConstructor
@Slf4j
public class CardPreviewController {

    private final CardPreviewService service;

    @GetMapping
    public ResponseEntity<Page<CardPreviewReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/cardpreviews - Finding all CardPreviews");
        Page<CardPreviewReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardPreviewReadDto>> findAllList() {
        log.info("GET /api/v1/cardpreviews/all - Finding all CardPreviews as list");
        List<CardPreviewReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardPreviewReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/cardpreviews/{} - Finding CardPreview by id", id);
        CardPreviewReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CardPreviewReadDto> create(@Valid @RequestBody CardPreviewCreateDto createDto) {
        log.info("POST /api/v1/cardpreviews - Creating new CardPreview: {}", createDto);
        CardPreviewReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardPreviewReadDto> update(@PathVariable Long id, @Valid @RequestBody CardPreviewUpdateDto updateDto) {
        log.info("PUT /api/v1/cardpreviews/{} - Updating CardPreview: {}", id, updateDto);
        CardPreviewReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/cardpreviews/{} - Deleting CardPreview", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CardPreviewReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/cardpreviews/{}/suspend - Suspending CardPreview", id);
        CardPreviewReadDto suspended = service.suspendCardPreview(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CardPreviewReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/cardpreviews/{}/activate - Activating CardPreview", id);
        CardPreviewReadDto activated = service.activateCardPreview(id);
        return ResponseEntity.ok(activated);
    }

}
