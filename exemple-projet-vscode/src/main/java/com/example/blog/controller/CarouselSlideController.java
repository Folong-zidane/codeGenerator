package com.example.blog.controller;

import com.example.blog.dto.CarouselSlideCreateDto;
import com.example.blog.dto.CarouselSlideReadDto;
import com.example.blog.dto.CarouselSlideUpdateDto;
import com.example.blog.service.CarouselSlideService;
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
@RequestMapping("/api/v1/carouselslides")
@RequiredArgsConstructor
@Slf4j
public class CarouselSlideController {

    private final CarouselSlideService service;

    @GetMapping
    public ResponseEntity<Page<CarouselSlideReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/carouselslides - Finding all CarouselSlides");
        Page<CarouselSlideReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarouselSlideReadDto>> findAllList() {
        log.info("GET /api/v1/carouselslides/all - Finding all CarouselSlides as list");
        List<CarouselSlideReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarouselSlideReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/carouselslides/{} - Finding CarouselSlide by id", id);
        CarouselSlideReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CarouselSlideReadDto> create(@Valid @RequestBody CarouselSlideCreateDto createDto) {
        log.info("POST /api/v1/carouselslides - Creating new CarouselSlide: {}", createDto);
        CarouselSlideReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarouselSlideReadDto> update(@PathVariable Long id, @Valid @RequestBody CarouselSlideUpdateDto updateDto) {
        log.info("PUT /api/v1/carouselslides/{} - Updating CarouselSlide: {}", id, updateDto);
        CarouselSlideReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/carouselslides/{} - Deleting CarouselSlide", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CarouselSlideReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/carouselslides/{}/suspend - Suspending CarouselSlide", id);
        CarouselSlideReadDto suspended = service.suspendCarouselSlide(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CarouselSlideReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/carouselslides/{}/activate - Activating CarouselSlide", id);
        CarouselSlideReadDto activated = service.activateCarouselSlide(id);
        return ResponseEntity.ok(activated);
    }

}
