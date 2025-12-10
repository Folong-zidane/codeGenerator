package com.example.blog.service;

import com.example.blog.entity.Cache;
import com.example.blog.repository.CacheRepository;
import com.example.blog.dto.CacheCreateDto;
import com.example.blog.dto.CacheReadDto;
import com.example.blog.dto.CacheUpdateDto;
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

import com.example.blog.enums.CacheStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CacheService {

    private final CacheRepository repository;

    @Transactional(readOnly = true)
    public List<CacheReadDto> findAll() {
        log.debug("Finding all Caches");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CacheReadDto> findById(Long id) {
        log.debug("Finding Cache by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CacheReadDto getById(Long id) {
        log.debug("Getting Cache by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Cache", id));
    }

    public CacheReadDto create(CacheCreateDto createDto) {
        log.info("Creating new Cache: {}", createDto);
        Cache entity = convertToEntity(createDto);
        Cache saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CacheReadDto update(Long id, CacheUpdateDto updateDto) {
        log.info("Updating Cache with id: {}", id);
        Cache existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cache", id));
        
        updateEntityFromDto(existing, updateDto);
        Cache updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Cache with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Cache", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CacheReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Cache with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CacheReadDto convertToReadDto(Cache entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CacheReadDto();
    }

    private Cache convertToEntity(CacheCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Cache();
    }

    private void updateEntityFromDto(Cache entity, CacheUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CacheReadDto suspendCache(Long id) {
        log.info("Suspending Cache with id: {}", id);
        Cache entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cache", id));
        entity.suspend();
        Cache updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CacheReadDto activateCache(Long id) {
        log.info("Activating Cache with id: {}", id);
        Cache entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cache", id));
        entity.activate();
        Cache updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
