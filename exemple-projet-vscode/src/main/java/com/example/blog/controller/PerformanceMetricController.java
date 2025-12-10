package com.example.blog.controller;

import com.example.blog.dto.PerformanceMetricCreateDto;
import com.example.blog.dto.PerformanceMetricReadDto;
import com.example.blog.dto.PerformanceMetricUpdateDto;
import com.example.blog.service.PerformanceMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/performancemetrics")
@RequiredArgsConstructor
public class PerformanceMetricController {

    private final PerformanceMetricService service;

    @GetMapping
    public ResponseEntity<Page<PerformanceMetricReadDto>> findAll(Pageable pageable) {        Page<PerformanceMetricReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PerformanceMetricReadDto>> findAllList() {        List<PerformanceMetricReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceMetricReadDto> findById(@PathVariable Long id) {        PerformanceMetricReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PerformanceMetricReadDto> create(@Valid @RequestBody PerformanceMetricCreateDto createDto) {        PerformanceMetricReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceMetricReadDto> update(@PathVariable Long id, @Valid @RequestBody PerformanceMetricUpdateDto updateDto) {        PerformanceMetricReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PerformanceMetricReadDto> suspend(@PathVariable Long id) {        PerformanceMetricReadDto suspended = service.suspendPerformanceMetric(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PerformanceMetricReadDto> activate(@PathVariable Long id) {        PerformanceMetricReadDto activated = service.activatePerformanceMetric(id);
        return ResponseEntity.ok(activated);
    }

}
