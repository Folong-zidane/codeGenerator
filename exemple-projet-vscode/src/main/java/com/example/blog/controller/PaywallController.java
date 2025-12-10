package com.example.blog.controller;

import com.example.blog.dto.PaywallCreateDto;
import com.example.blog.dto.PaywallReadDto;
import com.example.blog.dto.PaywallUpdateDto;
import com.example.blog.service.PaywallService;
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
@RequestMapping("/api/v1/paywalls")
@RequiredArgsConstructor
@Slf4j
public class PaywallController {

    private final PaywallService service;

    @GetMapping
    public ResponseEntity<Page<PaywallReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/paywalls - Finding all Paywalls");
        Page<PaywallReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaywallReadDto>> findAllList() {
        log.info("GET /api/v1/paywalls/all - Finding all Paywalls as list");
        List<PaywallReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaywallReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/paywalls/{} - Finding Paywall by id", id);
        PaywallReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaywallReadDto> create(@Valid @RequestBody PaywallCreateDto createDto) {
        log.info("POST /api/v1/paywalls - Creating new Paywall: {}", createDto);
        PaywallReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaywallReadDto> update(@PathVariable Long id, @Valid @RequestBody PaywallUpdateDto updateDto) {
        log.info("PUT /api/v1/paywalls/{} - Updating Paywall: {}", id, updateDto);
        PaywallReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/paywalls/{} - Deleting Paywall", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PaywallReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/paywalls/{}/suspend - Suspending Paywall", id);
        PaywallReadDto suspended = service.suspendPaywall(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PaywallReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/paywalls/{}/activate - Activating Paywall", id);
        PaywallReadDto activated = service.activatePaywall(id);
        return ResponseEntity.ok(activated);
    }

}
