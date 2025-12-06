package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.PayPalPaymentCreateDto;
import com.ecommerce.complex.dto.PayPalPaymentReadDto;
import com.ecommerce.complex.dto.PayPalPaymentUpdateDto;
import com.ecommerce.complex.service.PayPalPaymentService;
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
@RequestMapping("/api/v1/paypalpayments")
@RequiredArgsConstructor
@Slf4j
public class PayPalPaymentController {

    private final PayPalPaymentService service;

    @GetMapping
    public ResponseEntity<Page<PayPalPaymentReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/paypalpayments - Finding all PayPalPayments");
        Page<PayPalPaymentReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PayPalPaymentReadDto>> findAllList() {
        log.info("GET /api/v1/paypalpayments/all - Finding all PayPalPayments as list");
        List<PayPalPaymentReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayPalPaymentReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/paypalpayments/{} - Finding PayPalPayment by id", id);
        PayPalPaymentReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PayPalPaymentReadDto> create(@Valid @RequestBody PayPalPaymentCreateDto createDto) {
        log.info("POST /api/v1/paypalpayments - Creating new PayPalPayment: {}", createDto);
        PayPalPaymentReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayPalPaymentReadDto> update(@PathVariable Long id, @Valid @RequestBody PayPalPaymentUpdateDto updateDto) {
        log.info("PUT /api/v1/paypalpayments/{} - Updating PayPalPayment: {}", id, updateDto);
        PayPalPaymentReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/paypalpayments/{} - Deleting PayPalPayment", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PayPalPaymentReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/paypalpayments/{}/suspend - Suspending PayPalPayment", id);
        PayPalPaymentReadDto suspended = service.suspendPayPalPayment(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PayPalPaymentReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/paypalpayments/{}/activate - Activating PayPalPayment", id);
        PayPalPaymentReadDto activated = service.activatePayPalPayment(id);
        return ResponseEntity.ok(activated);
    }

}
