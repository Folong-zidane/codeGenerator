package com.example.blog.controller;

import com.example.blog.dto.CategoryFilterCreateDto;
import com.example.blog.dto.CategoryFilterReadDto;
import com.example.blog.dto.CategoryFilterUpdateDto;
import com.example.blog.service.CategoryFilterService;
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
@RequestMapping("/api/v1/categoryfilters")
@RequiredArgsConstructor
@Slf4j
public class CategoryFilterController {

    private final CategoryFilterService service;

    @GetMapping
    public ResponseEntity<Page<CategoryFilterReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/categoryfilters - Finding all CategoryFilters");
        Page<CategoryFilterReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryFilterReadDto>> findAllList() {
        log.info("GET /api/v1/categoryfilters/all - Finding all CategoryFilters as list");
        List<CategoryFilterReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryFilterReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/categoryfilters/{} - Finding CategoryFilter by id", id);
        CategoryFilterReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryFilterReadDto> create(@Valid @RequestBody CategoryFilterCreateDto createDto) {
        log.info("POST /api/v1/categoryfilters - Creating new CategoryFilter: {}", createDto);
        CategoryFilterReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryFilterReadDto> update(@PathVariable Long id, @Valid @RequestBody CategoryFilterUpdateDto updateDto) {
        log.info("PUT /api/v1/categoryfilters/{} - Updating CategoryFilter: {}", id, updateDto);
        CategoryFilterReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/categoryfilters/{} - Deleting CategoryFilter", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CategoryFilterReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/categoryfilters/{}/suspend - Suspending CategoryFilter", id);
        CategoryFilterReadDto suspended = service.suspendCategoryFilter(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryFilterReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/categoryfilters/{}/activate - Activating CategoryFilter", id);
        CategoryFilterReadDto activated = service.activateCategoryFilter(id);
        return ResponseEntity.ok(activated);
    }

}
