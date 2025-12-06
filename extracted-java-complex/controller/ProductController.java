package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.ProductCreateDto;
import com.ecommerce.complex.dto.ProductReadDto;
import com.ecommerce.complex.dto.ProductUpdateDto;
import com.ecommerce.complex.service.ProductService;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/products - Finding all Products");
        Page<ProductReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductReadDto>> findAllList() {
        log.info("GET /api/v1/products/all - Finding all Products as list");
        List<ProductReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/products/{} - Finding Product by id", id);
        ProductReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProductReadDto> create(@Valid @RequestBody ProductCreateDto createDto) {
        log.info("POST /api/v1/products - Creating new Product: {}", createDto);
        ProductReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductReadDto> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDto updateDto) {
        log.info("PUT /api/v1/products/{} - Updating Product: {}", id, updateDto);
        ProductReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/products/{} - Deleting Product", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ProductReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/products/{}/suspend - Suspending Product", id);
        ProductReadDto suspended = service.suspendProduct(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/products/{}/activate - Activating Product", id);
        ProductReadDto activated = service.activateProduct(id);
        return ResponseEntity.ok(activated);
    }

}
