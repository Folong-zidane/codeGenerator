package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.CreditCardPaymentCreateDto;
import com.ecommerce.complex.dto.CreditCardPaymentReadDto;
import com.ecommerce.complex.dto.CreditCardPaymentUpdateDto;
import com.ecommerce.complex.service.CreditCardPaymentService;
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
@RequestMapping("/api/v1/creditcardpayments")
@RequiredArgsConstructor
@Slf4j
public class CreditCardPaymentController {

    private final CreditCardPaymentService service;

    @GetMapping
    public ResponseEntity<Page<CreditCardPaymentReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/creditcardpayments - Finding all CreditCardPayments");
        Page<CreditCardPaymentReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CreditCardPaymentReadDto>> findAllList() {
        log.info("GET /api/v1/creditcardpayments/all - Finding all CreditCardPayments as list");
        List<CreditCardPaymentReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardPaymentReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/creditcardpayments/{} - Finding CreditCardPayment by id", id);
        CreditCardPaymentReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CreditCardPaymentReadDto> create(@Valid @RequestBody CreditCardPaymentCreateDto createDto) {
        log.info("POST /api/v1/creditcardpayments - Creating new CreditCardPayment: {}", createDto);
        CreditCardPaymentReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardPaymentReadDto> update(@PathVariable Long id, @Valid @RequestBody CreditCardPaymentUpdateDto updateDto) {
        log.info("PUT /api/v1/creditcardpayments/{} - Updating CreditCardPayment: {}", id, updateDto);
        CreditCardPaymentReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/creditcardpayments/{} - Deleting CreditCardPayment", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CreditCardPaymentReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/creditcardpayments/{}/suspend - Suspending CreditCardPayment", id);
        CreditCardPaymentReadDto suspended = service.suspendCreditCardPayment(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CreditCardPaymentReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/creditcardpayments/{}/activate - Activating CreditCardPayment", id);
        CreditCardPaymentReadDto activated = service.activateCreditCardPayment(id);
        return ResponseEntity.ok(activated);
    }

}
