package com.example.blog.service;

import com.example.blog.entity.PWAConfig;
import com.example.blog.repository.PWAConfigRepository;
import com.example.blog.dto.PWAConfigCreateDto;
import com.example.blog.dto.PWAConfigReadDto;
import com.example.blog.dto.PWAConfigUpdateDto;
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

import com.example.blog.enums.PWAConfigStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PWAConfigService {

    private final PWAConfigRepository repository;

    @Transactional(readOnly = true)
    public List<PWAConfigReadDto> findAll() {
        log.debug("Finding all PWAConfigs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PWAConfigReadDto> findById(Long id) {
        log.debug("Finding PWAConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PWAConfigReadDto getById(Long id) {
        log.debug("Getting PWAConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("PWAConfig", id));
    }

    public PWAConfigReadDto create(PWAConfigCreateDto createDto) {
        log.info("Creating new PWAConfig: {}", createDto);
        PWAConfig entity = convertToEntity(createDto);
        PWAConfig saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PWAConfigReadDto update(Long id, PWAConfigUpdateDto updateDto) {
        log.info("Updating PWAConfig with id: {}", id);
        PWAConfig existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PWAConfig", id));
        
        updateEntityFromDto(existing, updateDto);
        PWAConfig updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting PWAConfig with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PWAConfig", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PWAConfigReadDto> findAll(Pageable pageable) {
        log.debug("Finding all PWAConfig with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PWAConfigReadDto convertToReadDto(PWAConfig entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PWAConfigReadDto();
    }

    private PWAConfig convertToEntity(PWAConfigCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new PWAConfig();
    }

    private void updateEntityFromDto(PWAConfig entity, PWAConfigUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PWAConfigReadDto suspendPWAConfig(Long id) {
        log.info("Suspending PWAConfig with id: {}", id);
        PWAConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PWAConfig", id));
        entity.suspend();
        PWAConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PWAConfigReadDto activatePWAConfig(Long id) {
        log.info("Activating PWAConfig with id: {}", id);
        PWAConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PWAConfig", id));
        entity.activate();
        PWAConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
