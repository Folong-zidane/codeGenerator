package com.example.blog.controller;

import com.example.blog.dto.HomepageLayoutCreateDto;
import com.example.blog.dto.HomepageLayoutReadDto;
import com.example.blog.dto.HomepageLayoutUpdateDto;
import com.example.blog.service.HomepageLayoutService;
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
@RequestMapping("/api/v1/homepagelayouts")
@RequiredArgsConstructor
@Slf4j
public class HomepageLayoutController {

    private final HomepageLayoutService service;

    @GetMapping
    public ResponseEntity<Page<HomepageLayoutReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/homepagelayouts - Finding all HomepageLayouts");
        Page<HomepageLayoutReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<HomepageLayoutReadDto>> findAllList() {
        log.info("GET /api/v1/homepagelayouts/all - Finding all HomepageLayouts as list");
        List<HomepageLayoutReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomepageLayoutReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/homepagelayouts/{} - Finding HomepageLayout by id", id);
        HomepageLayoutReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<HomepageLayoutReadDto> create(@Valid @RequestBody HomepageLayoutCreateDto createDto) {
        log.info("POST /api/v1/homepagelayouts - Creating new HomepageLayout: {}", createDto);
        HomepageLayoutReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomepageLayoutReadDto> update(@PathVariable Long id, @Valid @RequestBody HomepageLayoutUpdateDto updateDto) {
        log.info("PUT /api/v1/homepagelayouts/{} - Updating HomepageLayout: {}", id, updateDto);
        HomepageLayoutReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/homepagelayouts/{} - Deleting HomepageLayout", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<HomepageLayoutReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/homepagelayouts/{}/suspend - Suspending HomepageLayout", id);
        HomepageLayoutReadDto suspended = service.suspendHomepageLayout(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<HomepageLayoutReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/homepagelayouts/{}/activate - Activating HomepageLayout", id);
        HomepageLayoutReadDto activated = service.activateHomepageLayout(id);
        return ResponseEntity.ok(activated);
    }

}
