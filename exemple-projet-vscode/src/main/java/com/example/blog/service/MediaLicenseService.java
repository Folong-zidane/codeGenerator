package com.example.blog.service;

import com.example.blog.entity.MediaLicense;
import com.example.blog.repository.MediaLicenseRepository;
import com.example.blog.dto.MediaLicenseCreateDto;
import com.example.blog.dto.MediaLicenseReadDto;
import com.example.blog.dto.MediaLicenseUpdateDto;
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

import com.example.blog.enums.MediaLicenseStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaLicenseService {

    private final MediaLicenseRepository repository;

    @Transactional(readOnly = true)
    public List<MediaLicenseReadDto> findAll() {
        log.debug("Finding all MediaLicenses");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MediaLicenseReadDto> findById(Long id) {
        log.debug("Finding MediaLicense by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MediaLicenseReadDto getById(Long id) {
        log.debug("Getting MediaLicense by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MediaLicense", id));
    }

    public MediaLicenseReadDto create(MediaLicenseCreateDto createDto) {
        log.info("Creating new MediaLicense: {}", createDto);
        MediaLicense entity = convertToEntity(createDto);
        MediaLicense saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MediaLicenseReadDto update(Long id, MediaLicenseUpdateDto updateDto) {
        log.info("Updating MediaLicense with id: {}", id);
        MediaLicense existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaLicense", id));
        
        updateEntityFromDto(existing, updateDto);
        MediaLicense updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting MediaLicense with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MediaLicense", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MediaLicenseReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MediaLicense with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MediaLicenseReadDto convertToReadDto(MediaLicense entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaLicenseReadDto();
    }

    private MediaLicense convertToEntity(MediaLicenseCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaLicense();
    }

    private void updateEntityFromDto(MediaLicense entity, MediaLicenseUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MediaLicenseReadDto suspendMediaLicense(Long id) {
        log.info("Suspending MediaLicense with id: {}", id);
        MediaLicense entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaLicense", id));
        entity.suspend();
        MediaLicense updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MediaLicenseReadDto activateMediaLicense(Long id) {
        log.info("Activating MediaLicense with id: {}", id);
        MediaLicense entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaLicense", id));
        entity.activate();
        MediaLicense updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
