package com.example.blog.service;

import com.example.blog.entity.SSOConfig;
import com.example.blog.repository.SSOConfigRepository;
import com.example.blog.dto.SSOConfigCreateDto;
import com.example.blog.dto.SSOConfigReadDto;
import com.example.blog.dto.SSOConfigUpdateDto;
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

import com.example.blog.enums.SSOConfigStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SSOConfigService {

    private final SSOConfigRepository repository;

    @Transactional(readOnly = true)
    public List<SSOConfigReadDto> findAll() {
        log.debug("Finding all SSOConfigs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SSOConfigReadDto> findById(Long id) {
        log.debug("Finding SSOConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public SSOConfigReadDto getById(Long id) {
        log.debug("Getting SSOConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("SSOConfig", id));
    }

    public SSOConfigReadDto create(SSOConfigCreateDto createDto) {
        log.info("Creating new SSOConfig: {}", createDto);
        SSOConfig entity = convertToEntity(createDto);
        SSOConfig saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public SSOConfigReadDto update(Long id, SSOConfigUpdateDto updateDto) {
        log.info("Updating SSOConfig with id: {}", id);
        SSOConfig existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SSOConfig", id));
        
        updateEntityFromDto(existing, updateDto);
        SSOConfig updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting SSOConfig with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SSOConfig", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SSOConfigReadDto> findAll(Pageable pageable) {
        log.debug("Finding all SSOConfig with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private SSOConfigReadDto convertToReadDto(SSOConfig entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new SSOConfigReadDto();
    }

    private SSOConfig convertToEntity(SSOConfigCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new SSOConfig();
    }

    private void updateEntityFromDto(SSOConfig entity, SSOConfigUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public SSOConfigReadDto suspendSSOConfig(Long id) {
        log.info("Suspending SSOConfig with id: {}", id);
        SSOConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SSOConfig", id));
        entity.suspend();
        SSOConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public SSOConfigReadDto activateSSOConfig(Long id) {
        log.info("Activating SSOConfig with id: {}", id);
        SSOConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SSOConfig", id));
        entity.activate();
        SSOConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
