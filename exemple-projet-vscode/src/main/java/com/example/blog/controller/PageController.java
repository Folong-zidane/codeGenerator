package com.example.blog.controller;

import com.example.blog.dto.PageCreateDto;
import com.example.blog.dto.PageReadDto;
import com.example.blog.dto.PageUpdateDto;
import com.example.blog.service.PageService;
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
@RequestMapping("/api/v1/pages")
@RequiredArgsConstructor
@Slf4j
public class PageController {

    private final PageService service;

    @GetMapping
    public ResponseEntity<Page<PageReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/pages - Finding all Pages");
        Page<PageReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PageReadDto>> findAllList() {
        log.info("GET /api/v1/pages/all - Finding all Pages as list");
        List<PageReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PageReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/pages/{} - Finding Page by id", id);
        PageReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PageReadDto> create(@Valid @RequestBody PageCreateDto createDto) {
        log.info("POST /api/v1/pages - Creating new Page: {}", createDto);
        PageReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PageReadDto> update(@PathVariable Long id, @Valid @RequestBody PageUpdateDto updateDto) {
        log.info("PUT /api/v1/pages/{} - Updating Page: {}", id, updateDto);
        PageReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/pages/{} - Deleting Page", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PageReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/pages/{}/suspend - Suspending Page", id);
        PageReadDto suspended = service.suspendPage(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PageReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/pages/{}/activate - Activating Page", id);
        PageReadDto activated = service.activatePage(id);
        return ResponseEntity.ok(activated);
    }

}
