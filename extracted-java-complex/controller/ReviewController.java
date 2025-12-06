package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.ReviewCreateDto;
import com.ecommerce.complex.dto.ReviewReadDto;
import com.ecommerce.complex.dto.ReviewUpdateDto;
import com.ecommerce.complex.service.ReviewService;
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
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService service;

    @GetMapping
    public ResponseEntity<Page<ReviewReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/reviews - Finding all Reviews");
        Page<ReviewReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewReadDto>> findAllList() {
        log.info("GET /api/v1/reviews/all - Finding all Reviews as list");
        List<ReviewReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/reviews/{} - Finding Review by id", id);
        ReviewReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ReviewReadDto> create(@Valid @RequestBody ReviewCreateDto createDto) {
        log.info("POST /api/v1/reviews - Creating new Review: {}", createDto);
        ReviewReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewReadDto> update(@PathVariable Long id, @Valid @RequestBody ReviewUpdateDto updateDto) {
        log.info("PUT /api/v1/reviews/{} - Updating Review: {}", id, updateDto);
        ReviewReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/reviews/{} - Deleting Review", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ReviewReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/reviews/{}/suspend - Suspending Review", id);
        ReviewReadDto suspended = service.suspendReview(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ReviewReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/reviews/{}/activate - Activating Review", id);
        ReviewReadDto activated = service.activateReview(id);
        return ResponseEntity.ok(activated);
    }

}
