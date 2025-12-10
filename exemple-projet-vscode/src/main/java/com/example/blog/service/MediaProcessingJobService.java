package com.example.blog.service;

import com.example.blog.entity.MediaProcessingJob;
import com.example.blog.repository.MediaProcessingJobRepository;
import com.example.blog.dto.MediaProcessingJobCreateDto;
import com.example.blog.dto.MediaProcessingJobReadDto;
import com.example.blog.dto.MediaProcessingJobUpdateDto;
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

import com.example.blog.enums.MediaProcessingJobStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MediaProcessingJobService {

    private final MediaProcessingJobRepository repository;

    @Transactional(readOnly = true)
    public List<MediaProcessingJobReadDto> findAll() {
        log.debug("Finding all MediaProcessingJobs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MediaProcessingJobReadDto> findById(Long id) {
        log.debug("Finding MediaProcessingJob by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MediaProcessingJobReadDto getById(Long id) {
        log.debug("Getting MediaProcessingJob by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MediaProcessingJob", id));
    }

    public MediaProcessingJobReadDto create(MediaProcessingJobCreateDto createDto) {
        log.info("Creating new MediaProcessingJob: {}", createDto);
        MediaProcessingJob entity = convertToEntity(createDto);
        MediaProcessingJob saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MediaProcessingJobReadDto update(Long id, MediaProcessingJobUpdateDto updateDto) {
        log.info("Updating MediaProcessingJob with id: {}", id);
        MediaProcessingJob existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaProcessingJob", id));
        
        updateEntityFromDto(existing, updateDto);
        MediaProcessingJob updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting MediaProcessingJob with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MediaProcessingJob", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MediaProcessingJobReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MediaProcessingJob with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MediaProcessingJobReadDto convertToReadDto(MediaProcessingJob entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaProcessingJobReadDto();
    }

    private MediaProcessingJob convertToEntity(MediaProcessingJobCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MediaProcessingJob();
    }

    private void updateEntityFromDto(MediaProcessingJob entity, MediaProcessingJobUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MediaProcessingJobReadDto suspendMediaProcessingJob(Long id) {
        log.info("Suspending MediaProcessingJob with id: {}", id);
        MediaProcessingJob entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaProcessingJob", id));
        entity.suspend();
        MediaProcessingJob updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MediaProcessingJobReadDto activateMediaProcessingJob(Long id) {
        log.info("Activating MediaProcessingJob with id: {}", id);
        MediaProcessingJob entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaProcessingJob", id));
        entity.activate();
        MediaProcessingJob updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
