package com.example.blog.service;

import com.example.blog.entity.AuditLog;
import com.example.blog.repository.AuditLogRepository;
import com.example.blog.dto.AuditLogCreateDto;
import com.example.blog.dto.AuditLogReadDto;
import com.example.blog.dto.AuditLogUpdateDto;
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

import com.example.blog.enums.AuditLogStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditLogService {

    private final AuditLogRepository repository;

    @Transactional(readOnly = true)
    public List<AuditLogReadDto> findAll() {
        log.debug("Finding all AuditLogs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AuditLogReadDto> findById(Long id) {
        log.debug("Finding AuditLog by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public AuditLogReadDto getById(Long id) {
        log.debug("Getting AuditLog by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("AuditLog", id));
    }

    public AuditLogReadDto create(AuditLogCreateDto createDto) {
        log.info("Creating new AuditLog: {}", createDto);
        AuditLog entity = convertToEntity(createDto);
        AuditLog saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public AuditLogReadDto update(Long id, AuditLogUpdateDto updateDto) {
        log.info("Updating AuditLog with id: {}", id);
        AuditLog existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AuditLog", id));
        
        updateEntityFromDto(existing, updateDto);
        AuditLog updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting AuditLog with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("AuditLog", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogReadDto> findAll(Pageable pageable) {
        log.debug("Finding all AuditLog with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private AuditLogReadDto convertToReadDto(AuditLog entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new AuditLogReadDto();
    }

    private AuditLog convertToEntity(AuditLogCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new AuditLog();
    }

    private void updateEntityFromDto(AuditLog entity, AuditLogUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public AuditLogReadDto suspendAuditLog(Long id) {
        log.info("Suspending AuditLog with id: {}", id);
        AuditLog entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AuditLog", id));
        entity.suspend();
        AuditLog updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public AuditLogReadDto activateAuditLog(Long id) {
        log.info("Activating AuditLog with id: {}", id);
        AuditLog entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AuditLog", id));
        entity.activate();
        AuditLog updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
