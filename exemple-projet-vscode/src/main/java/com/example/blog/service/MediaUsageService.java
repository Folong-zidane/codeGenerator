package com.example.blog.service;

import com.example.blog.entity.MediaUsage;
import com.example.blog.repository.MediaUsageRepository;
import com.example.blog.dto.MediaUsageCreateDto;
import com.example.blog.dto.MediaUsageReadDto;
import com.example.blog.dto.MediaUsageUpdateDto;
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

import com.example.blog.enums.MediaUsageStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaUsageService {

    private final MediaUsageRepository repository;

    @Transactional(readOnly = true)
    public List<MediaUsageReadDto> findAll() {
        log.debug("Finding all MediaUsages");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MediaUsageReadDto> findById(Long id) {
        log.debug("Finding MediaUsage by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MediaUsageReadDto getById(Long id) {
        log.debug("Getting MediaUsage by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MediaUsage", id));
    }

    public MediaUsageReadDto create(MediaUsageCreateDto createDto) {
        log.info("Creating new MediaUsage: {}", createDto);
        MediaUsage entity = convertToEntity(createDto);
        MediaUsage saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MediaUsageReadDto update(Long id, MediaUsageUpdateDto updateDto) {
        log.info("Updating MediaUsage with id: {}", id);
        MediaUsage existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaUsage", id));
        
        updateEntityFromDto(existing, updateDto);
        MediaUsage updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting MediaUsage with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MediaUsage", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MediaUsageReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MediaUsage with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MediaUsageReadDto convertToReadDto(MediaUsage entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaUsageReadDto();
    }

    private MediaUsage convertToEntity(MediaUsageCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaUsage();
    }

    private void updateEntityFromDto(MediaUsage entity, MediaUsageUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MediaUsageReadDto suspendMediaUsage(Long id) {
        log.info("Suspending MediaUsage with id: {}", id);
        MediaUsage entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaUsage", id));
        entity.suspend();
        MediaUsage updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MediaUsageReadDto activateMediaUsage(Long id) {
        log.info("Activating MediaUsage with id: {}", id);
        MediaUsage entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaUsage", id));
        entity.activate();
        MediaUsage updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
