package com.example.blog.service;

import com.example.blog.entity.HomepageLayout;
import com.example.blog.repository.HomepageLayoutRepository;
import com.example.blog.dto.HomepageLayoutCreateDto;
import com.example.blog.dto.HomepageLayoutReadDto;
import com.example.blog.dto.HomepageLayoutUpdateDto;
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

import com.example.blog.enums.HomepageLayoutStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HomepageLayoutService {

    private final HomepageLayoutRepository repository;

    @Transactional(readOnly = true)
    public List<HomepageLayoutReadDto> findAll() {
        log.debug("Finding all HomepageLayouts");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<HomepageLayoutReadDto> findById(Long id) {
        log.debug("Finding HomepageLayout by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public HomepageLayoutReadDto getById(Long id) {
        log.debug("Getting HomepageLayout by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("HomepageLayout", id));
    }

    public HomepageLayoutReadDto create(HomepageLayoutCreateDto createDto) {
        log.info("Creating new HomepageLayout: {}", createDto);
        HomepageLayout entity = convertToEntity(createDto);
        HomepageLayout saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public HomepageLayoutReadDto update(Long id, HomepageLayoutUpdateDto updateDto) {
        log.info("Updating HomepageLayout with id: {}", id);
        HomepageLayout existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("HomepageLayout", id));
        
        updateEntityFromDto(existing, updateDto);
        HomepageLayout updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting HomepageLayout with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("HomepageLayout", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<HomepageLayoutReadDto> findAll(Pageable pageable) {
        log.debug("Finding all HomepageLayout with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private HomepageLayoutReadDto convertToReadDto(HomepageLayout entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new HomepageLayoutReadDto();
    }

    private HomepageLayout convertToEntity(HomepageLayoutCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new HomepageLayout();
    }

    private void updateEntityFromDto(HomepageLayout entity, HomepageLayoutUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public HomepageLayoutReadDto suspendHomepageLayout(Long id) {
        log.info("Suspending HomepageLayout with id: {}", id);
        HomepageLayout entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("HomepageLayout", id));
        entity.suspend();
        HomepageLayout updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public HomepageLayoutReadDto activateHomepageLayout(Long id) {
        log.info("Activating HomepageLayout with id: {}", id);
        HomepageLayout entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("HomepageLayout", id));
        entity.activate();
        HomepageLayout updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
