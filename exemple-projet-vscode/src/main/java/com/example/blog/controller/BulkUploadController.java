package com.example.blog.controller;

import com.example.blog.dto.BulkUploadCreateDto;
import com.example.blog.dto.BulkUploadReadDto;
import com.example.blog.dto.BulkUploadUpdateDto;
import com.example.blog.service.BulkUploadService;
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
@RequestMapping("/api/v1/bulkuploads")
@RequiredArgsConstructor
@Slf4j
public class BulkUploadController {

    private final BulkUploadService service;

    @GetMapping
    public ResponseEntity<Page<BulkUploadReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/bulkuploads - Finding all BulkUploads");
        Page<BulkUploadReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BulkUploadReadDto>> findAllList() {
        log.info("GET /api/v1/bulkuploads/all - Finding all BulkUploads as list");
        List<BulkUploadReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BulkUploadReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/bulkuploads/{} - Finding BulkUpload by id", id);
        BulkUploadReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BulkUploadReadDto> create(@Valid @RequestBody BulkUploadCreateDto createDto) {
        log.info("POST /api/v1/bulkuploads - Creating new BulkUpload: {}", createDto);
        BulkUploadReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BulkUploadReadDto> update(@PathVariable Long id, @Valid @RequestBody BulkUploadUpdateDto updateDto) {
        log.info("PUT /api/v1/bulkuploads/{} - Updating BulkUpload: {}", id, updateDto);
        BulkUploadReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/bulkuploads/{} - Deleting BulkUpload", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<BulkUploadReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/bulkuploads/{}/suspend - Suspending BulkUpload", id);
        BulkUploadReadDto suspended = service.suspendBulkUpload(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<BulkUploadReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/bulkuploads/{}/activate - Activating BulkUpload", id);
        BulkUploadReadDto activated = service.activateBulkUpload(id);
        return ResponseEntity.ok(activated);
    }

}
