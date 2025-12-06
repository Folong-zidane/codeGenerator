package com.test.complete.controller;

import com.test.complete.entity.Order;
import com.test.complete.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    public List<Order> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody Order entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order entity) {
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
    public ResponseEntity<Order> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(service.suspendOrder(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Order> activate(@PathVariable Long id) {
        return ResponseEntity.ok(service.activateOrder(id));
    }

}
