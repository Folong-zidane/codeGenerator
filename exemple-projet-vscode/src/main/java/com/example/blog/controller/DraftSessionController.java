package com.example.blog.controller;

import com.example.blog.dto.DraftSessionCreateDto;
import com.example.blog.dto.DraftSessionReadDto;
import com.example.blog.dto.DraftSessionUpdateDto;
import com.example.blog.service.DraftSessionService;
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
@RequestMapping("/api/v1/draftsessions")
@RequiredArgsConstructor
@Slf4j
public class DraftSessionController {

    private final DraftSessionService service;

    @GetMapping
    public ResponseEntity<Page<DraftSessionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/draftsessions - Finding all DraftSessions");
        Page<DraftSessionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DraftSessionReadDto>> findAllList() {
        log.info("GET /api/v1/draftsessions/all - Finding all DraftSessions as list");
        List<DraftSessionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DraftSessionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/draftsessions/{} - Finding DraftSession by id", id);
        DraftSessionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DraftSessionReadDto> create(@Valid @RequestBody DraftSessionCreateDto createDto) {
        log.info("POST /api/v1/draftsessions - Creating new DraftSession: {}", createDto);
        DraftSessionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DraftSessionReadDto> update(@PathVariable Long id, @Valid @RequestBody DraftSessionUpdateDto updateDto) {
        log.info("PUT /api/v1/draftsessions/{} - Updating DraftSession: {}", id, updateDto);
        DraftSessionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/draftsessions/{} - Deleting DraftSession", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<DraftSessionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/draftsessions/{}/suspend - Suspending DraftSession", id);
        DraftSessionReadDto suspended = service.suspendDraftSession(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DraftSessionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/draftsessions/{}/activate - Activating DraftSession", id);
        DraftSessionReadDto activated = service.activateDraftSession(id);
        return ResponseEntity.ok(activated);
    }

}
