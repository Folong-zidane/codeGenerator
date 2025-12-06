package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.OrderItem;
import com.ecommerce.complex.repository.OrderItemRepository;
import com.ecommerce.complex.dto.OrderItemCreateDto;
import com.ecommerce.complex.dto.OrderItemReadDto;
import com.ecommerce.complex.dto.OrderItemUpdateDto;
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

import com.ecommerce.complex.enums.OrderItemStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderItemService {

    private final OrderItemRepository repository;

    @Transactional(readOnly = true)
    public List<OrderItemReadDto> findAll() {
        log.debug("Finding all OrderItems");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OrderItemReadDto> findById(Long id) {
        log.debug("Finding OrderItem by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public OrderItemReadDto getById(Long id) {
        log.debug("Getting OrderItem by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem", id));
    }

    public OrderItemReadDto create(OrderItemCreateDto createDto) {
        log.info("Creating new OrderItem: {}", createDto);
        OrderItem entity = convertToEntity(createDto);
        OrderItem saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public OrderItemReadDto update(Long id, OrderItemUpdateDto updateDto) {
        log.info("Updating OrderItem with id: {}", id);
        OrderItem existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem", id));
        
        updateEntityFromDto(existing, updateDto);
        OrderItem updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting OrderItem with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<OrderItemReadDto> findAll(Pageable pageable) {
        log.debug("Finding all OrderItem with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private OrderItemReadDto convertToReadDto(OrderItem entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new OrderItemReadDto();
    }

    private OrderItem convertToEntity(OrderItemCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new OrderItem();
    }

    private void updateEntityFromDto(OrderItem entity, OrderItemUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public OrderItemReadDto suspendOrderItem(Long id) {
        log.info("Suspending OrderItem with id: {}", id);
        OrderItem entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem", id));
        entity.suspend();
        OrderItem updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public OrderItemReadDto activateOrderItem(Long id) {
        log.info("Activating OrderItem with id: {}", id);
        OrderItem entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OrderItem", id));
        entity.activate();
        OrderItem updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
