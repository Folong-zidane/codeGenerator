package com.example.blog.controller;

import com.example.blog.dto.CategoryCreateDto;
import com.example.blog.dto.CategoryReadDto;
import com.example.blog.dto.CategoryUpdateDto;
import com.example.blog.service.CategoryService;
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
@RequestMapping("/api/v1/categorys")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/categorys - Finding all Categorys");
        Page<CategoryReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryReadDto>> findAllList() {
        log.info("GET /api/v1/categorys/all - Finding all Categorys as list");
        List<CategoryReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/categorys/{} - Finding Category by id", id);
        CategoryReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryReadDto> create(@Valid @RequestBody CategoryCreateDto createDto) {
        log.info("POST /api/v1/categorys - Creating new Category: {}", createDto);
        CategoryReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryReadDto> update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDto updateDto) {
        log.info("PUT /api/v1/categorys/{} - Updating Category: {}", id, updateDto);
        CategoryReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/categorys/{} - Deleting Category", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CategoryReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/categorys/{}/suspend - Suspending Category", id);
        CategoryReadDto suspended = service.suspendCategory(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/categorys/{}/activate - Activating Category", id);
        CategoryReadDto activated = service.activateCategory(id);
        return ResponseEntity.ok(activated);
    }

}
