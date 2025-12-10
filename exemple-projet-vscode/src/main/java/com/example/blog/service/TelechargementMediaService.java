package com.example.blog.service;

import com.example.blog.entity.TelechargementMedia;
import com.example.blog.repository.TelechargementMediaRepository;
import com.example.blog.dto.TelechargementMediaCreateDto;
import com.example.blog.dto.TelechargementMediaReadDto;
import com.example.blog.dto.TelechargementMediaUpdateDto;
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

import com.example.blog.enums.TelechargementMediaStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TelechargementMediaService {

    private final TelechargementMediaRepository repository;

    @Transactional(readOnly = true)
    public List<TelechargementMediaReadDto> findAll() {
        log.debug("Finding all TelechargementMedias");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TelechargementMediaReadDto> findById(Long id) {
        log.debug("Finding TelechargementMedia by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public TelechargementMediaReadDto getById(Long id) {
        log.debug("Getting TelechargementMedia by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("TelechargementMedia", id));
    }

    public TelechargementMediaReadDto create(TelechargementMediaCreateDto createDto) {
        log.info("Creating new TelechargementMedia: {}", createDto);
        TelechargementMedia entity = convertToEntity(createDto);
        TelechargementMedia saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public TelechargementMediaReadDto update(Long id, TelechargementMediaUpdateDto updateDto) {
        log.info("Updating TelechargementMedia with id: {}", id);
        TelechargementMedia existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TelechargementMedia", id));
        
        updateEntityFromDto(existing, updateDto);
        TelechargementMedia updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting TelechargementMedia with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TelechargementMedia", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TelechargementMediaReadDto> findAll(Pageable pageable) {
        log.debug("Finding all TelechargementMedia with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private TelechargementMediaReadDto convertToReadDto(TelechargementMedia entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new TelechargementMediaReadDto();
    }

    private TelechargementMedia convertToEntity(TelechargementMediaCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new TelechargementMedia();
    }

    private void updateEntityFromDto(TelechargementMedia entity, TelechargementMediaUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public TelechargementMediaReadDto suspendTelechargementMedia(Long id) {
        log.info("Suspending TelechargementMedia with id: {}", id);
        TelechargementMedia entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TelechargementMedia", id));
        entity.suspend();
        TelechargementMedia updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public TelechargementMediaReadDto activateTelechargementMedia(Long id) {
        log.info("Activating TelechargementMedia with id: {}", id);
        TelechargementMedia entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TelechargementMedia", id));
        entity.activate();
        TelechargementMedia updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
