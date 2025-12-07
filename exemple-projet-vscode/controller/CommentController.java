package com.example.blog.controller;

import com.example.blog.dto.CommentCreateDto;
import com.example.blog.dto.CommentReadDto;
import com.example.blog.dto.CommentUpdateDto;
import com.example.blog.service.CommentService;
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
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService service;

    @GetMapping
    public ResponseEntity<Page<CommentReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/comments - Finding all Comments");
        Page<CommentReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentReadDto>> findAllList() {
        log.info("GET /api/v1/comments/all - Finding all Comments as list");
        List<CommentReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/comments/{} - Finding Comment by id", id);
        CommentReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CommentReadDto> create(@Valid @RequestBody CommentCreateDto createDto) {
        log.info("POST /api/v1/comments - Creating new Comment: {}", createDto);
        CommentReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentReadDto> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateDto updateDto) {
        log.info("PUT /api/v1/comments/{} - Updating Comment: {}", id, updateDto);
        CommentReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/comments/{} - Deleting Comment", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CommentReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/comments/{}/suspend - Suspending Comment", id);
        CommentReadDto suspended = service.suspendComment(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CommentReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/comments/{}/activate - Activating Comment", id);
        CommentReadDto activated = service.activateComment(id);
        return ResponseEntity.ok(activated);
    }

}
