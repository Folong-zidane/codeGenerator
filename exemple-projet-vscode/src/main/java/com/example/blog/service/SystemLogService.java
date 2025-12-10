package com.example.blog.service;

import com.example.blog.entity.SystemLog;
import com.example.blog.repository.SystemLogRepository;
import com.example.blog.dto.SystemLogCreateDto;
import com.example.blog.dto.SystemLogReadDto;
import com.example.blog.dto.SystemLogUpdateDto;
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

import com.example.blog.enums.SystemLogStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SystemLogService {

    private final SystemLogRepository repository;

    @Transactional(readOnly = true)
    public List<SystemLogReadDto> findAll() {
        log.debug("Finding all SystemLogs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SystemLogReadDto> findById(Long id) {
        log.debug("Finding SystemLog by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public SystemLogReadDto getById(Long id) {
        log.debug("Getting SystemLog by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("SystemLog", id));
    }

    public SystemLogReadDto create(SystemLogCreateDto createDto) {
        log.info("Creating new SystemLog: {}", createDto);
        SystemLog entity = convertToEntity(createDto);
        SystemLog saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public SystemLogReadDto update(Long id, SystemLogUpdateDto updateDto) {
        log.info("Updating SystemLog with id: {}", id);
        SystemLog existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SystemLog", id));
        
        updateEntityFromDto(existing, updateDto);
        SystemLog updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting SystemLog with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("SystemLog", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SystemLogReadDto> findAll(Pageable pageable) {
        log.debug("Finding all SystemLog with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private SystemLogReadDto convertToReadDto(SystemLog entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new SystemLogReadDto();
    }

    private SystemLog convertToEntity(SystemLogCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new SystemLog();
    }

    private void updateEntityFromDto(SystemLog entity, SystemLogUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public SystemLogReadDto suspendSystemLog(Long id) {
        log.info("Suspending SystemLog with id: {}", id);
        SystemLog entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SystemLog", id));
        entity.suspend();
        SystemLog updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public SystemLogReadDto activateSystemLog(Long id) {
        log.info("Activating SystemLog with id: {}", id);
        SystemLog entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SystemLog", id));
        entity.activate();
        SystemLog updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
