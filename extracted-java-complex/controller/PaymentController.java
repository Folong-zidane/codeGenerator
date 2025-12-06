package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.PaymentCreateDto;
import com.ecommerce.complex.dto.PaymentReadDto;
import com.ecommerce.complex.dto.PaymentUpdateDto;
import com.ecommerce.complex.service.PaymentService;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public ResponseEntity<Page<PaymentReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/payments - Finding all Payments");
        Page<PaymentReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentReadDto>> findAllList() {
        log.info("GET /api/v1/payments/all - Finding all Payments as list");
        List<PaymentReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/payments/{} - Finding Payment by id", id);
        PaymentReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentReadDto> create(@Valid @RequestBody PaymentCreateDto createDto) {
        log.info("POST /api/v1/payments - Creating new Payment: {}", createDto);
        PaymentReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentReadDto> update(@PathVariable Long id, @Valid @RequestBody PaymentUpdateDto updateDto) {
        log.info("PUT /api/v1/payments/{} - Updating Payment: {}", id, updateDto);
        PaymentReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/payments/{} - Deleting Payment", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PaymentReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/payments/{}/suspend - Suspending Payment", id);
        PaymentReadDto suspended = service.suspendPayment(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PaymentReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/payments/{}/activate - Activating Payment", id);
        PaymentReadDto activated = service.activatePayment(id);
        return ResponseEntity.ok(activated);
    }

}
