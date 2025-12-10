package com.example.blog.controller;

import com.example.blog.dto.DocumentPreviewCreateDto;
import com.example.blog.dto.DocumentPreviewReadDto;
import com.example.blog.dto.DocumentPreviewUpdateDto;
import com.example.blog.service.DocumentPreviewService;
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
@RequestMapping("/api/v1/documentpreviews")
@RequiredArgsConstructor
@Slf4j
public class DocumentPreviewController {

    private final DocumentPreviewService service;

    @GetMapping
    public ResponseEntity<Page<DocumentPreviewReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/documentpreviews - Finding all DocumentPreviews");
        Page<DocumentPreviewReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentPreviewReadDto>> findAllList() {
        log.info("GET /api/v1/documentpreviews/all - Finding all DocumentPreviews as list");
        List<DocumentPreviewReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentPreviewReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/documentpreviews/{} - Finding DocumentPreview by id", id);
        DocumentPreviewReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DocumentPreviewReadDto> create(@Valid @RequestBody DocumentPreviewCreateDto createDto) {
        log.info("POST /api/v1/documentpreviews - Creating new DocumentPreview: {}", createDto);
        DocumentPreviewReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentPreviewReadDto> update(@PathVariable Long id, @Valid @RequestBody DocumentPreviewUpdateDto updateDto) {
        log.info("PUT /api/v1/documentpreviews/{} - Updating DocumentPreview: {}", id, updateDto);
        DocumentPreviewReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/documentpreviews/{} - Deleting DocumentPreview", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<DocumentPreviewReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/documentpreviews/{}/suspend - Suspending DocumentPreview", id);
        DocumentPreviewReadDto suspended = service.suspendDocumentPreview(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DocumentPreviewReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/documentpreviews/{}/activate - Activating DocumentPreview", id);
        DocumentPreviewReadDto activated = service.activateDocumentPreview(id);
        return ResponseEntity.ok(activated);
    }

}
