package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.OrderCreateDto;
import com.ecommerce.complex.dto.OrderReadDto;
import com.ecommerce.complex.dto.OrderUpdateDto;
import com.ecommerce.complex.service.OrderService;
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
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @GetMapping
    public ResponseEntity<Page<OrderReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/orders - Finding all Orders");
        Page<OrderReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderReadDto>> findAllList() {
        log.info("GET /api/v1/orders/all - Finding all Orders as list");
        List<OrderReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/orders/{} - Finding Order by id", id);
        OrderReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OrderReadDto> create(@Valid @RequestBody OrderCreateDto createDto) {
        log.info("POST /api/v1/orders - Creating new Order: {}", createDto);
        OrderReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderReadDto> update(@PathVariable Long id, @Valid @RequestBody OrderUpdateDto updateDto) {
        log.info("PUT /api/v1/orders/{} - Updating Order: {}", id, updateDto);
        OrderReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/orders/{} - Deleting Order", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<OrderReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/orders/{}/suspend - Suspending Order", id);
        OrderReadDto suspended = service.suspendOrder(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<OrderReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/orders/{}/activate - Activating Order", id);
        OrderReadDto activated = service.activateOrder(id);
        return ResponseEntity.ok(activated);
    }

}
