package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.InventoryCreateDto;
import com.ecommerce.complex.dto.InventoryReadDto;
import com.ecommerce.complex.dto.InventoryUpdateDto;
import com.ecommerce.complex.service.InventoryService;
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
@RequestMapping("/api/v1/inventorys")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    public ResponseEntity<Page<InventoryReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/inventorys - Finding all Inventorys");
        Page<InventoryReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryReadDto>> findAllList() {
        log.info("GET /api/v1/inventorys/all - Finding all Inventorys as list");
        List<InventoryReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/inventorys/{} - Finding Inventory by id", id);
        InventoryReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<InventoryReadDto> create(@Valid @RequestBody InventoryCreateDto createDto) {
        log.info("POST /api/v1/inventorys - Creating new Inventory: {}", createDto);
        InventoryReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryReadDto> update(@PathVariable Long id, @Valid @RequestBody InventoryUpdateDto updateDto) {
        log.info("PUT /api/v1/inventorys/{} - Updating Inventory: {}", id, updateDto);
        InventoryReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/inventorys/{} - Deleting Inventory", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<InventoryReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/inventorys/{}/suspend - Suspending Inventory", id);
        InventoryReadDto suspended = service.suspendInventory(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<InventoryReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/inventorys/{}/activate - Activating Inventory", id);
        InventoryReadDto activated = service.activateInventory(id);
        return ResponseEntity.ok(activated);
    }

}
