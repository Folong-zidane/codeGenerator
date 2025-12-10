package com.example.blog.controller;

import com.example.blog.dto.FeaturedItemCreateDto;
import com.example.blog.dto.FeaturedItemReadDto;
import com.example.blog.dto.FeaturedItemUpdateDto;
import com.example.blog.service.FeaturedItemService;
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
@RequestMapping("/api/v1/featureditems")
@RequiredArgsConstructor
@Slf4j
public class FeaturedItemController {

    private final FeaturedItemService service;

    @GetMapping
    public ResponseEntity<Page<FeaturedItemReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/featureditems - Finding all FeaturedItems");
        Page<FeaturedItemReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeaturedItemReadDto>> findAllList() {
        log.info("GET /api/v1/featureditems/all - Finding all FeaturedItems as list");
        List<FeaturedItemReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeaturedItemReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/featureditems/{} - Finding FeaturedItem by id", id);
        FeaturedItemReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<FeaturedItemReadDto> create(@Valid @RequestBody FeaturedItemCreateDto createDto) {
        log.info("POST /api/v1/featureditems - Creating new FeaturedItem: {}", createDto);
        FeaturedItemReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeaturedItemReadDto> update(@PathVariable Long id, @Valid @RequestBody FeaturedItemUpdateDto updateDto) {
        log.info("PUT /api/v1/featureditems/{} - Updating FeaturedItem: {}", id, updateDto);
        FeaturedItemReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/featureditems/{} - Deleting FeaturedItem", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<FeaturedItemReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/featureditems/{}/suspend - Suspending FeaturedItem", id);
        FeaturedItemReadDto suspended = service.suspendFeaturedItem(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<FeaturedItemReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/featureditems/{}/activate - Activating FeaturedItem", id);
        FeaturedItemReadDto activated = service.activateFeaturedItem(id);
        return ResponseEntity.ok(activated);
    }

}
