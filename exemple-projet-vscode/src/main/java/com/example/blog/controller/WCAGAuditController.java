package com.example.blog.controller;

import com.example.blog.dto.WCAGAuditCreateDto;
import com.example.blog.dto.WCAGAuditReadDto;
import com.example.blog.dto.WCAGAuditUpdateDto;
import com.example.blog.service.WCAGAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wcagaudits")
@RequiredArgsConstructor
public class WCAGAuditController {

    private final WCAGAuditService service;

    @GetMapping
    public ResponseEntity<Page<WCAGAuditReadDto>> findAll(Pageable pageable) {        Page<WCAGAuditReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WCAGAuditReadDto>> findAllList() {        List<WCAGAuditReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WCAGAuditReadDto> findById(@PathVariable Long id) {        WCAGAuditReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<WCAGAuditReadDto> create(@Valid @RequestBody WCAGAuditCreateDto createDto) {        WCAGAuditReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WCAGAuditReadDto> update(@PathVariable Long id, @Valid @RequestBody WCAGAuditUpdateDto updateDto) {        WCAGAuditReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<WCAGAuditReadDto> suspend(@PathVariable Long id) {        WCAGAuditReadDto suspended = service.suspendWCAGAudit(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<WCAGAuditReadDto> activate(@PathVariable Long id) {        WCAGAuditReadDto activated = service.activateWCAGAudit(id);
        return ResponseEntity.ok(activated);
    }

}
