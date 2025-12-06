package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Order;
import com.ecommerce.complex.repository.OrderRepository;
import com.ecommerce.complex.dto.OrderCreateDto;
import com.ecommerce.complex.dto.OrderReadDto;
import com.ecommerce.complex.dto.OrderUpdateDto;
import com.ecommerce.complex.exception.EntityNotFoundException;
import com.ecommerce.complex.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.complex.enums.OrderStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository repository;

    @Transactional(readOnly = true)
    public List<OrderReadDto> findAll() {
        log.debug("Finding all Orders");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderReadDto> findById(Long id) {
        log.debug("Finding Order by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public OrderReadDto getById(Long id) {
        log.debug("Getting Order by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Order", id));
    }

    public OrderReadDto create(OrderCreateDto createDto) {
        log.info("Creating new Order: {}", createDto);
        Order entity = convertToEntity(createDto);
        Order saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public OrderReadDto update(Long id, OrderUpdateDto updateDto) {
        log.info("Updating Order with id: {}", id);
        Order existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order", id));
        
        updateEntityFromDto(existing, updateDto);
        Order updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Order with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Order", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<OrderReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Order with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private OrderReadDto convertToReadDto(Order entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new OrderReadDto();
    }

    private Order convertToEntity(OrderCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Order();
    }

    private void updateEntityFromDto(Order entity, OrderUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public OrderReadDto suspendOrder(Long id) {
        log.info("Suspending Order with id: {}", id);
        Order entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order", id));
        entity.suspend();
        Order updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public OrderReadDto activateOrder(Long id) {
        log.info("Activating Order with id: {}", id);
        Order entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order", id));
        entity.activate();
        Order updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
