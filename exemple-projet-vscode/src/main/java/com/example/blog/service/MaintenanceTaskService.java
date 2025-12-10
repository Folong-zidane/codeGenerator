package com.example.blog.service;

import com.example.blog.entity.MaintenanceTask;
import com.example.blog.repository.MaintenanceTaskRepository;
import com.example.blog.dto.MaintenanceTaskCreateDto;
import com.example.blog.dto.MaintenanceTaskReadDto;
import com.example.blog.dto.MaintenanceTaskUpdateDto;
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

import com.example.blog.enums.MaintenanceTaskStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaintenanceTaskService {

    private final MaintenanceTaskRepository repository;

    @Transactional(readOnly = true)
    public List<MaintenanceTaskReadDto> findAll() {
        log.debug("Finding all MaintenanceTasks");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MaintenanceTaskReadDto> findById(Long id) {
        log.debug("Finding MaintenanceTask by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MaintenanceTaskReadDto getById(Long id) {
        log.debug("Getting MaintenanceTask by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("MaintenanceTask", id));
    }

    public MaintenanceTaskReadDto create(MaintenanceTaskCreateDto createDto) {
        log.info("Creating new MaintenanceTask: {}", createDto);
        MaintenanceTask entity = convertToEntity(createDto);
        MaintenanceTask saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MaintenanceTaskReadDto update(Long id, MaintenanceTaskUpdateDto updateDto) {
        log.info("Updating MaintenanceTask with id: {}", id);
        MaintenanceTask existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MaintenanceTask", id));
        
        updateEntityFromDto(existing, updateDto);
        MaintenanceTask updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting MaintenanceTask with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("MaintenanceTask", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MaintenanceTaskReadDto> findAll(Pageable pageable) {
        log.debug("Finding all MaintenanceTask with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MaintenanceTaskReadDto convertToReadDto(MaintenanceTask entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MaintenanceTaskReadDto();
    }

    private MaintenanceTask convertToEntity(MaintenanceTaskCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new MaintenanceTask();
    }

    private void updateEntityFromDto(MaintenanceTask entity, MaintenanceTaskUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MaintenanceTaskReadDto suspendMaintenanceTask(Long id) {
        log.info("Suspending MaintenanceTask with id: {}", id);
        MaintenanceTask entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MaintenanceTask", id));
        entity.suspend();
        MaintenanceTask updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MaintenanceTaskReadDto activateMaintenanceTask(Long id) {
        log.info("Activating MaintenanceTask with id: {}", id);
        MaintenanceTask entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MaintenanceTask", id));
        entity.activate();
        MaintenanceTask updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
