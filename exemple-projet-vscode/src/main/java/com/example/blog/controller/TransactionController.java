package com.example.blog.controller;

import com.example.blog.dto.TransactionCreateDto;
import com.example.blog.dto.TransactionReadDto;
import com.example.blog.dto.TransactionUpdateDto;
import com.example.blog.service.TransactionService;
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
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public ResponseEntity<Page<TransactionReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/transactions - Finding all Transactions");
        Page<TransactionReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionReadDto>> findAllList() {
        log.info("GET /api/v1/transactions/all - Finding all Transactions as list");
        List<TransactionReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/transactions/{} - Finding Transaction by id", id);
        TransactionReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TransactionReadDto> create(@Valid @RequestBody TransactionCreateDto createDto) {
        log.info("POST /api/v1/transactions - Creating new Transaction: {}", createDto);
        TransactionReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionReadDto> update(@PathVariable Long id, @Valid @RequestBody TransactionUpdateDto updateDto) {
        log.info("PUT /api/v1/transactions/{} - Updating Transaction: {}", id, updateDto);
        TransactionReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/transactions/{} - Deleting Transaction", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<TransactionReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/transactions/{}/suspend - Suspending Transaction", id);
        TransactionReadDto suspended = service.suspendTransaction(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TransactionReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/transactions/{}/activate - Activating Transaction", id);
        TransactionReadDto activated = service.activateTransaction(id);
        return ResponseEntity.ok(activated);
    }

}
