package com.example.blog.controller;

import com.example.blog.dto.PostCreateDto;
import com.example.blog.dto.PostReadDto;
import com.example.blog.dto.PostUpdateDto;
import com.example.blog.service.PostService;
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
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService service;

    @GetMapping
    public ResponseEntity<Page<PostReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/posts - Finding all Posts");
        Page<PostReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostReadDto>> findAllList() {
        log.info("GET /api/v1/posts/all - Finding all Posts as list");
        List<PostReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/posts/{} - Finding Post by id", id);
        PostReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PostReadDto> create(@Valid @RequestBody PostCreateDto createDto) {
        log.info("POST /api/v1/posts - Creating new Post: {}", createDto);
        PostReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostReadDto> update(@PathVariable Long id, @Valid @RequestBody PostUpdateDto updateDto) {
        log.info("PUT /api/v1/posts/{} - Updating Post: {}", id, updateDto);
        PostReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/posts/{} - Deleting Post", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PostReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/posts/{}/suspend - Suspending Post", id);
        PostReadDto suspended = service.suspendPost(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PostReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/posts/{}/activate - Activating Post", id);
        PostReadDto activated = service.activatePost(id);
        return ResponseEntity.ok(activated);
    }

}
