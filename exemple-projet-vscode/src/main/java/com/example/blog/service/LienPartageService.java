package com.example.blog.service;

import com.example.blog.entity.LienPartage;
import com.example.blog.repository.LienPartageRepository;
import com.example.blog.dto.LienPartageCreateDto;
import com.example.blog.dto.LienPartageReadDto;
import com.example.blog.dto.LienPartageUpdateDto;
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

import com.example.blog.enums.LienPartageStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LienPartageService {

    private final LienPartageRepository repository;

    @Transactional(readOnly = true)
    public List<LienPartageReadDto> findAll() {
        log.debug("Finding all LienPartages");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<LienPartageReadDto> findById(Long id) {
        log.debug("Finding LienPartage by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public LienPartageReadDto getById(Long id) {
        log.debug("Getting LienPartage by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("LienPartage", id));
    }

    public LienPartageReadDto create(LienPartageCreateDto createDto) {
        log.info("Creating new LienPartage: {}", createDto);
        LienPartage entity = convertToEntity(createDto);
        LienPartage saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public LienPartageReadDto update(Long id, LienPartageUpdateDto updateDto) {
        log.info("Updating LienPartage with id: {}", id);
        LienPartage existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LienPartage", id));
        
        updateEntityFromDto(existing, updateDto);
        LienPartage updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting LienPartage with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("LienPartage", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<LienPartageReadDto> findAll(Pageable pageable) {
        log.debug("Finding all LienPartage with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private LienPartageReadDto convertToReadDto(LienPartage entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new LienPartageReadDto();
    }

    private LienPartage convertToEntity(LienPartageCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new LienPartage();
    }

    private void updateEntityFromDto(LienPartage entity, LienPartageUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public LienPartageReadDto suspendLienPartage(Long id) {
        log.info("Suspending LienPartage with id: {}", id);
        LienPartage entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LienPartage", id));
        entity.suspend();
        LienPartage updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public LienPartageReadDto activateLienPartage(Long id) {
        log.info("Activating LienPartage with id: {}", id);
        LienPartage entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LienPartage", id));
        entity.activate();
        LienPartage updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
