package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Entity;
import com.ecommerce.complex.repository.EntityRepository;
import com.ecommerce.complex.dto.EntityCreateDto;
import com.ecommerce.complex.dto.EntityReadDto;
import com.ecommerce.complex.dto.EntityUpdateDto;
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

import com.ecommerce.complex.enums.EntityStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EntityService {

    private final EntityRepository repository;

    @Transactional(readOnly = true)
    public List<EntityReadDto> findAll() {
        log.debug("Finding all Entitys");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EntityReadDto> findById(Long id) {
        log.debug("Finding Entity by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public EntityReadDto getById(Long id) {
        log.debug("Getting Entity by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Entity", id));
    }

    public EntityReadDto create(EntityCreateDto createDto) {
        log.info("Creating new Entity: {}", createDto);
        Entity entity = convertToEntity(createDto);
        Entity saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public EntityReadDto update(Long id, EntityUpdateDto updateDto) {
        log.info("Updating Entity with id: {}", id);
        Entity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entity", id));
        
        updateEntityFromDto(existing, updateDto);
        Entity updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Entity with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<EntityReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Entity with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private EntityReadDto convertToReadDto(Entity entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new EntityReadDto();
    }

    private Entity convertToEntity(EntityCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Entity();
    }

    private void updateEntityFromDto(Entity entity, EntityUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public EntityReadDto suspendEntity(Long id) {
        log.info("Suspending Entity with id: {}", id);
        Entity entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entity", id));
        entity.suspend();
        Entity updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public EntityReadDto activateEntity(Long id) {
        log.info("Activating Entity with id: {}", id);
        Entity entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Entity", id));
        entity.activate();
        Entity updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
