package com.example.blog.controller;

import com.example.blog.dto.PartageConfigCreateDto;
import com.example.blog.dto.PartageConfigReadDto;
import com.example.blog.dto.PartageConfigUpdateDto;
import com.example.blog.service.PartageConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/partageconfigs")
@RequiredArgsConstructor
public class PartageConfigController {

    private final PartageConfigService service;

    @GetMapping
    public ResponseEntity<Page<PartageConfigReadDto>> findAll(Pageable pageable) {        Page<PartageConfigReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartageConfigReadDto>> findAllList() {        List<PartageConfigReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartageConfigReadDto> findById(@PathVariable Long id) {        PartageConfigReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PartageConfigReadDto> create(@Valid @RequestBody PartageConfigCreateDto createDto) {        PartageConfigReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartageConfigReadDto> update(@PathVariable Long id, @Valid @RequestBody PartageConfigUpdateDto updateDto) {        PartageConfigReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PartageConfigReadDto> suspend(@PathVariable Long id) {        PartageConfigReadDto suspended = service.suspendPartageConfig(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PartageConfigReadDto> activate(@PathVariable Long id) {        PartageConfigReadDto activated = service.activatePartageConfig(id);
        return ResponseEntity.ok(activated);
    }

}
