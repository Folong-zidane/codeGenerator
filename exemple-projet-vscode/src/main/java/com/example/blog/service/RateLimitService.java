package com.example.blog.service;

import com.example.blog.entity.RateLimit;
import com.example.blog.repository.RateLimitRepository;
import com.example.blog.dto.RateLimitCreateDto;
import com.example.blog.dto.RateLimitReadDto;
import com.example.blog.dto.RateLimitUpdateDto;
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

import com.example.blog.enums.RateLimitStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RateLimitService {

    private final RateLimitRepository repository;

    @Transactional(readOnly = true)
    public List<RateLimitReadDto> findAll() {
        log.debug("Finding all RateLimits");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RateLimitReadDto> findById(Long id) {
        log.debug("Finding RateLimit by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public RateLimitReadDto getById(Long id) {
        log.debug("Getting RateLimit by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("RateLimit", id));
    }

    public RateLimitReadDto create(RateLimitCreateDto createDto) {
        log.info("Creating new RateLimit: {}", createDto);
        RateLimit entity = convertToEntity(createDto);
        RateLimit saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public RateLimitReadDto update(Long id, RateLimitUpdateDto updateDto) {
        log.info("Updating RateLimit with id: {}", id);
        RateLimit existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("RateLimit", id));
        
        updateEntityFromDto(existing, updateDto);
        RateLimit updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting RateLimit with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("RateLimit", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<RateLimitReadDto> findAll(Pageable pageable) {
        log.debug("Finding all RateLimit with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private RateLimitReadDto convertToReadDto(RateLimit entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new RateLimitReadDto();
    }

    private RateLimit convertToEntity(RateLimitCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new RateLimit();
    }

    private void updateEntityFromDto(RateLimit entity, RateLimitUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public RateLimitReadDto suspendRateLimit(Long id) {
        log.info("Suspending RateLimit with id: {}", id);
        RateLimit entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("RateLimit", id));
        entity.suspend();
        RateLimit updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public RateLimitReadDto activateRateLimit(Long id) {
        log.info("Activating RateLimit with id: {}", id);
        RateLimit entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("RateLimit", id));
        entity.activate();
        RateLimit updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
