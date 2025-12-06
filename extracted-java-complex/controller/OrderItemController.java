package com.ecommerce.complex.controller;

import com.ecommerce.complex.dto.OrderItemCreateDto;
import com.ecommerce.complex.dto.OrderItemReadDto;
import com.ecommerce.complex.dto.OrderItemUpdateDto;
import com.ecommerce.complex.service.OrderItemService;
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
@RequestMapping("/api/v1/orderitems")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {

    private final OrderItemService service;

    @GetMapping
    public ResponseEntity<Page<OrderItemReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/orderitems - Finding all OrderItems");
        Page<OrderItemReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderItemReadDto>> findAllList() {
        log.info("GET /api/v1/orderitems/all - Finding all OrderItems as list");
        List<OrderItemReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/orderitems/{} - Finding OrderItem by id", id);
        OrderItemReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OrderItemReadDto> create(@Valid @RequestBody OrderItemCreateDto createDto) {
        log.info("POST /api/v1/orderitems - Creating new OrderItem: {}", createDto);
        OrderItemReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemReadDto> update(@PathVariable Long id, @Valid @RequestBody OrderItemUpdateDto updateDto) {
        log.info("PUT /api/v1/orderitems/{} - Updating OrderItem: {}", id, updateDto);
        OrderItemReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/orderitems/{} - Deleting OrderItem", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<OrderItemReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/orderitems/{}/suspend - Suspending OrderItem", id);
        OrderItemReadDto suspended = service.suspendOrderItem(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<OrderItemReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/orderitems/{}/activate - Activating OrderItem", id);
        OrderItemReadDto activated = service.activateOrderItem(id);
        return ResponseEntity.ok(activated);
    }

}
