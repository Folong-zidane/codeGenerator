package com.example.blog.service;

import com.example.blog.entity.MediaFile;
import com.example.blog.repository.MediaFileRepository;
import com.example.blog.dto.MediaFileCreateDto;
import com.example.blog.dto.MediaFileReadDto;
import com.example.blog.dto.MediaFileUpdateDto;
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

import com.example.blog.enums.MediaFileStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaFileService {

    private final MediaFileRepository repository;

    @Transactional(readOnly = true)
    public List<MediaFileReadDto> findAll() {
        log.debug("Finding all MediaFiles");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MediaFileReadDto> findById(UUID id) {
        log.debug("Finding MediaFile by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MediaFileReadDto getById(UUID id) {
        log.debug("Getting MediaFile by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile", id));
    }

    public MediaFileReadDto create(MediaFileCreateDto createDto) {
        log.info("Creating new MediaFile: {}", createDto);
        MediaFile entity = convertToEntity(createDto);
        MediaFile saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MediaFileReadDto update(UUID id, MediaFileUpdateDto updateDto) {
        log.info("Updating MediaFile with id: {}", id);
        MediaFile existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile", id));
        
        updateEntityFromDto(existing, updateDto);
        MediaFile updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(UUID id) {
        log.info("Deleting MediaFile with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MediaFile", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MediaFileReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MediaFile with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MediaFileReadDto convertToReadDto(MediaFile entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaFileReadDto();
    }

    private MediaFile convertToEntity(MediaFileCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaFile();
    }

    private void updateEntityFromDto(MediaFile entity, MediaFileUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MediaFileReadDto suspendMediaFile(UUID id) {
        log.info("Suspending MediaFile with id: {}", id);
        MediaFile entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile", id));
        entity.suspend();
        MediaFile updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MediaFileReadDto activateMediaFile(UUID id) {
        log.info("Activating MediaFile with id: {}", id);
        MediaFile entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile", id));
        entity.activate();
        MediaFile updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
