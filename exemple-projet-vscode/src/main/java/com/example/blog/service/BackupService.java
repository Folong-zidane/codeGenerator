package com.example.blog.service;

import com.example.blog.entity.Backup;
import com.example.blog.repository.BackupRepository;
import com.example.blog.dto.BackupCreateDto;
import com.example.blog.dto.BackupReadDto;
import com.example.blog.dto.BackupUpdateDto;
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

import com.example.blog.enums.BackupStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BackupService {

    private final BackupRepository repository;

    @Transactional(readOnly = true)
    public List<BackupReadDto> findAll() {
        log.debug("Finding all Backups");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BackupReadDto> findById(Long id) {
        log.debug("Finding Backup by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public BackupReadDto getById(Long id) {
        log.debug("Getting Backup by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Backup", id));
    }

    public BackupReadDto create(BackupCreateDto createDto) {
        log.info("Creating new Backup: {}", createDto);
        Backup entity = convertToEntity(createDto);
        Backup saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public BackupReadDto update(Long id, BackupUpdateDto updateDto) {
        log.info("Updating Backup with id: {}", id);
        Backup existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Backup", id));
        
        updateEntityFromDto(existing, updateDto);
        Backup updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Backup with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Backup", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<BackupReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Backup with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private BackupReadDto convertToReadDto(Backup entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new BackupReadDto();
    }

    private Backup convertToEntity(BackupCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Backup();
    }

    private void updateEntityFromDto(Backup entity, BackupUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public BackupReadDto suspendBackup(Long id) {
        log.info("Suspending Backup with id: {}", id);
        Backup entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Backup", id));
        entity.suspend();
        Backup updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public BackupReadDto activateBackup(Long id) {
        log.info("Activating Backup with id: {}", id);
        Backup entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Backup", id));
        entity.activate();
        Backup updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
