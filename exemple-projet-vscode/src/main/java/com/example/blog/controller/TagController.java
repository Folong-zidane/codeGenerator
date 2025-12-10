package com.example.blog.controller;

import com.example.blog.dto.TagCreateDto;
import com.example.blog.dto.TagReadDto;
import com.example.blog.dto.TagUpdateDto;
import com.example.blog.service.TagService;
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
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {

    private final TagService service;

    @GetMapping
    public ResponseEntity<Page<TagReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/tags - Finding all Tags");
        Page<TagReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TagReadDto>> findAllList() {
        log.info("GET /api/v1/tags/all - Finding all Tags as list");
        List<TagReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/tags/{} - Finding Tag by id", id);
        TagReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TagReadDto> create(@Valid @RequestBody TagCreateDto createDto) {
        log.info("POST /api/v1/tags - Creating new Tag: {}", createDto);
        TagReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagReadDto> update(@PathVariable Long id, @Valid @RequestBody TagUpdateDto updateDto) {
        log.info("PUT /api/v1/tags/{} - Updating Tag: {}", id, updateDto);
        TagReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/tags/{} - Deleting Tag", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<TagReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/tags/{}/suspend - Suspending Tag", id);
        TagReadDto suspended = service.suspendTag(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TagReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/tags/{}/activate - Activating Tag", id);
        TagReadDto activated = service.activateTag(id);
        return ResponseEntity.ok(activated);
    }

}
