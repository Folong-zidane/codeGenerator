package com.example.blog.service;

import com.example.blog.entity.OfflineQueue;
import com.example.blog.repository.OfflineQueueRepository;
import com.example.blog.dto.OfflineQueueCreateDto;
import com.example.blog.dto.OfflineQueueReadDto;
import com.example.blog.dto.OfflineQueueUpdateDto;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.blog.enums.OfflineQueueStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OfflineQueueService {

    private final OfflineQueueRepository repository;

    @Transactional(readOnly = true)
    public List<OfflineQueueReadDto> findAll() {
        log.debug("Finding all OfflineQueues");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<OfflineQueueReadDto> findById(Long id) {
        log.debug("Finding OfflineQueue by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public OfflineQueueReadDto getById(Long id) {
        log.debug("Getting OfflineQueue by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("OfflineQueue", id));
    }

    public OfflineQueueReadDto create(OfflineQueueCreateDto createDto) {
        log.info("Creating new OfflineQueue: {}", createDto);
        OfflineQueue entity = convertToEntity(createDto);
        OfflineQueue saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public OfflineQueueReadDto update(Long id, OfflineQueueUpdateDto updateDto) {
        log.info("Updating OfflineQueue with id: {}", id);
        OfflineQueue existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OfflineQueue", id));
        
        updateEntityFromDto(existing, updateDto);
        OfflineQueue updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting OfflineQueue with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("OfflineQueue", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<OfflineQueueReadDto> findAll(Pageable pageable) {
        log.debug("Finding all OfflineQueue with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private OfflineQueueReadDto convertToReadDto(OfflineQueue entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new OfflineQueueReadDto();
    }

    private OfflineQueue convertToEntity(OfflineQueueCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new OfflineQueue();
    }

    private void updateEntityFromDto(OfflineQueue entity, OfflineQueueUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public OfflineQueueReadDto suspendOfflineQueue(Long id) {
        log.info("Suspending OfflineQueue with id: {}", id);
        OfflineQueue entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OfflineQueue", id));
        entity.suspend();
        OfflineQueue updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public OfflineQueueReadDto activateOfflineQueue(Long id) {
        log.info("Activating OfflineQueue with id: {}", id);
        OfflineQueue entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("OfflineQueue", id));
        entity.activate();
        OfflineQueue updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
