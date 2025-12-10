package com.example.blog.controller;

import com.example.blog.dto.RealTimeUpdateCreateDto;
import com.example.blog.dto.RealTimeUpdateReadDto;
import com.example.blog.dto.RealTimeUpdateUpdateDto;
import com.example.blog.service.RealTimeUpdateService;
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
@RequestMapping("/api/v1/realtimeupdates")
@RequiredArgsConstructor
@Slf4j
public class RealTimeUpdateController {

    private final RealTimeUpdateService service;

    @GetMapping
    public ResponseEntity<Page<RealTimeUpdateReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/realtimeupdates - Finding all RealTimeUpdates");
        Page<RealTimeUpdateReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RealTimeUpdateReadDto>> findAllList() {
        log.info("GET /api/v1/realtimeupdates/all - Finding all RealTimeUpdates as list");
        List<RealTimeUpdateReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealTimeUpdateReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/realtimeupdates/{} - Finding RealTimeUpdate by id", id);
        RealTimeUpdateReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RealTimeUpdateReadDto> create(@Valid @RequestBody RealTimeUpdateCreateDto createDto) {
        log.info("POST /api/v1/realtimeupdates - Creating new RealTimeUpdate: {}", createDto);
        RealTimeUpdateReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealTimeUpdateReadDto> update(@PathVariable Long id, @Valid @RequestBody RealTimeUpdateUpdateDto updateDto) {
        log.info("PUT /api/v1/realtimeupdates/{} - Updating RealTimeUpdate: {}", id, updateDto);
        RealTimeUpdateReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/realtimeupdates/{} - Deleting RealTimeUpdate", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<RealTimeUpdateReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/realtimeupdates/{}/suspend - Suspending RealTimeUpdate", id);
        RealTimeUpdateReadDto suspended = service.suspendRealTimeUpdate(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RealTimeUpdateReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/realtimeupdates/{}/activate - Activating RealTimeUpdate", id);
        RealTimeUpdateReadDto activated = service.activateRealTimeUpdate(id);
        return ResponseEntity.ok(activated);
    }

}
