package com.example.blog.service;

import com.example.blog.entity.CDNConfig;
import com.example.blog.repository.CDNConfigRepository;
import com.example.blog.dto.CDNConfigCreateDto;
import com.example.blog.dto.CDNConfigReadDto;
import com.example.blog.dto.CDNConfigUpdateDto;
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

import com.example.blog.enums.CDNConfigStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CDNConfigService {

    private final CDNConfigRepository repository;

    @Transactional(readOnly = true)
    public List<CDNConfigReadDto> findAll() {
        log.debug("Finding all CDNConfigs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CDNConfigReadDto> findById(Long id) {
        log.debug("Finding CDNConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CDNConfigReadDto getById(Long id) {
        log.debug("Getting CDNConfig by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("CDNConfig", id));
    }

    public CDNConfigReadDto create(CDNConfigCreateDto createDto) {
        log.info("Creating new CDNConfig: {}", createDto);
        CDNConfig entity = convertToEntity(createDto);
        CDNConfig saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CDNConfigReadDto update(Long id, CDNConfigUpdateDto updateDto) {
        log.info("Updating CDNConfig with id: {}", id);
        CDNConfig existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CDNConfig", id));
        
        updateEntityFromDto(existing, updateDto);
        CDNConfig updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting CDNConfig with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CDNConfig", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CDNConfigReadDto> findAll(Pageable pageable) {
        log.debug("Finding all CDNConfig with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CDNConfigReadDto convertToReadDto(CDNConfig entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CDNConfigReadDto();
    }

    private CDNConfig convertToEntity(CDNConfigCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new CDNConfig();
    }

    private void updateEntityFromDto(CDNConfig entity, CDNConfigUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CDNConfigReadDto suspendCDNConfig(Long id) {
        log.info("Suspending CDNConfig with id: {}", id);
        CDNConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CDNConfig", id));
        entity.suspend();
        CDNConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CDNConfigReadDto activateCDNConfig(Long id) {
        log.info("Activating CDNConfig with id: {}", id);
        CDNConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CDNConfig", id));
        entity.activate();
        CDNConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
