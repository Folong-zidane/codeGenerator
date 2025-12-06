package com.test.fixed.controller;

import com.test.fixed.entity.Product;
import com.test.fixed.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product entity) {
        if (service.findById(id).isPresent()) {
            return ResponseEntity.ok(service.save(entity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<Product> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(service.suspendProduct(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Product> activate(@PathVariable Long id) {
        return ResponseEntity.ok(service.activateProduct(id));
    }

}
