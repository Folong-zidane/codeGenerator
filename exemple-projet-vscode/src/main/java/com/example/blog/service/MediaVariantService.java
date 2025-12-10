package com.example.blog.service;

import com.example.blog.entity.MediaVariant;
import com.example.blog.repository.MediaVariantRepository;
import com.example.blog.dto.MediaVariantCreateDto;
import com.example.blog.dto.MediaVariantReadDto;
import com.example.blog.dto.MediaVariantUpdateDto;
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
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.blog.enums.MediaVariantStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaVariantService {

    private final MediaVariantRepository repository;

    @Transactional(readOnly = true)
    public List<MediaVariantReadDto> findAll() {
        log.debug("Finding all MediaVariants");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MediaVariantReadDto> findById(UUID id) {
        log.debug("Finding MediaVariant by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MediaVariantReadDto getById(UUID id) {
        log.debug("Getting MediaVariant by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MediaVariant", id));
    }

    public MediaVariantReadDto create(MediaVariantCreateDto createDto) {
        log.info("Creating new MediaVariant: {}", createDto);
        MediaVariant entity = convertToEntity(createDto);
        MediaVariant saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MediaVariantReadDto update(UUID id, MediaVariantUpdateDto updateDto) {
        log.info("Updating MediaVariant with id: {}", id);
        MediaVariant existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaVariant", id));
        
        updateEntityFromDto(existing, updateDto);
        MediaVariant updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(UUID id) {
        log.info("Deleting MediaVariant with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MediaVariant", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MediaVariantReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MediaVariant with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MediaVariantReadDto convertToReadDto(MediaVariant entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaVariantReadDto();
    }

    private MediaVariant convertToEntity(MediaVariantCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaVariant();
    }

    private void updateEntityFromDto(MediaVariant entity, MediaVariantUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MediaVariantReadDto suspendMediaVariant(UUID id) {
        log.info("Suspending MediaVariant with id: {}", id);
        MediaVariant entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaVariant", id));
        entity.suspend();
        MediaVariant updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MediaVariantReadDto activateMediaVariant(UUID id) {
        log.info("Activating MediaVariant with id: {}", id);
        MediaVariant entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaVariant", id));
        entity.activate();
        MediaVariant updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
