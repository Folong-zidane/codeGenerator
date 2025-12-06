package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Inventory;
import com.ecommerce.complex.repository.InventoryRepository;
import com.ecommerce.complex.dto.InventoryCreateDto;
import com.ecommerce.complex.dto.InventoryReadDto;
import com.ecommerce.complex.dto.InventoryUpdateDto;
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

import com.ecommerce.complex.enums.InventoryStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public List<InventoryReadDto> findAll() {
        log.debug("Finding all Inventorys");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<InventoryReadDto> findById(Long id) {
        log.debug("Finding Inventory by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public InventoryReadDto getById(Long id) {
        log.debug("Getting Inventory by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Inventory", id));
    }

    public InventoryReadDto create(InventoryCreateDto createDto) {
        log.info("Creating new Inventory: {}", createDto);
        Inventory entity = convertToEntity(createDto);
        Inventory saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public InventoryReadDto update(Long id, InventoryUpdateDto updateDto) {
        log.info("Updating Inventory with id: {}", id);
        Inventory existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inventory", id));
        
        updateEntityFromDto(existing, updateDto);
        Inventory updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Inventory with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Inventory", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<InventoryReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Inventory with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private InventoryReadDto convertToReadDto(Inventory entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new InventoryReadDto();
    }

    private Inventory convertToEntity(InventoryCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Inventory();
    }

    private void updateEntityFromDto(Inventory entity, InventoryUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public InventoryReadDto suspendInventory(Long id) {
        log.info("Suspending Inventory with id: {}", id);
        Inventory entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inventory", id));
        entity.suspend();
        Inventory updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public InventoryReadDto activateInventory(Long id) {
        log.info("Activating Inventory with id: {}", id);
        Inventory entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inventory", id));
        entity.activate();
        Inventory updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
