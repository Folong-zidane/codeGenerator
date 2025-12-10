package com.example.blog.service;

import com.example.blog.entity.SEOConfig;
import com.example.blog.repository.SEOConfigRepository;
import com.example.blog.dto.SEOConfigCreateDto;
import com.example.blog.dto.SEOConfigReadDto;
import com.example.blog.dto.SEOConfigUpdateDto;
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

import com.example.blog.enums.SEOConfigStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SEOConfigService {

    private final SEOConfigRepository repository;

    @Transactional(readOnly = true)
    public List<SEOConfigReadDto> findAll() {
        log.debug("Finding all SEOConfigs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SEOConfigReadDto> findById(Long id) {
        log.debug("Finding SEOConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public SEOConfigReadDto getById(Long id) {
        log.debug("Getting SEOConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("SEOConfig", id));
    }

    public SEOConfigReadDto create(SEOConfigCreateDto createDto) {
        log.info("Creating new SEOConfig: {}", createDto);
        SEOConfig entity = convertToEntity(createDto);
        SEOConfig saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public SEOConfigReadDto update(Long id, SEOConfigUpdateDto updateDto) {
        log.info("Updating SEOConfig with id: {}", id);
        SEOConfig existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SEOConfig", id));
        
        updateEntityFromDto(existing, updateDto);
        SEOConfig updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting SEOConfig with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SEOConfig", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SEOConfigReadDto> findAll(Pageable pageable) {
        log.debug("Finding all SEOConfig with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private SEOConfigReadDto convertToReadDto(SEOConfig entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new SEOConfigReadDto();
    }

    private SEOConfig convertToEntity(SEOConfigCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new SEOConfig();
    }

    private void updateEntityFromDto(SEOConfig entity, SEOConfigUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public SEOConfigReadDto suspendSEOConfig(Long id) {
        log.info("Suspending SEOConfig with id: {}", id);
        SEOConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SEOConfig", id));
        entity.suspend();
        SEOConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public SEOConfigReadDto activateSEOConfig(Long id) {
        log.info("Activating SEOConfig with id: {}", id);
        SEOConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SEOConfig", id));
        entity.activate();
        SEOConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
