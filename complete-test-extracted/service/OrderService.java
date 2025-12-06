package com.test.complete.service;

import com.test.complete.entity.Order;
import com.test.complete.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.test.complete.enums.OrderStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository repository;

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    public Order save(Order entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Order suspendOrder(Long id) {
        Order entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        entity.suspend();
        return repository.save(entity);
    }

    public Order activateOrder(Long id) {
        Order entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        entity.activate();
        return repository.save(entity);
    }

}
