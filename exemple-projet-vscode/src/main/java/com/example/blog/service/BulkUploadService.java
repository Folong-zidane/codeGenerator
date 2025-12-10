package com.example.blog.service;

import com.example.blog.entity.BulkUpload;
import com.example.blog.repository.BulkUploadRepository;
import com.example.blog.dto.BulkUploadCreateDto;
import com.example.blog.dto.BulkUploadReadDto;
import com.example.blog.dto.BulkUploadUpdateDto;
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

import com.example.blog.enums.BulkUploadStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BulkUploadService {

    private final BulkUploadRepository repository;

    @Transactional(readOnly = true)
    public List<BulkUploadReadDto> findAll() {
        log.debug("Finding all BulkUploads");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BulkUploadReadDto> findById(Long id) {
        log.debug("Finding BulkUpload by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public BulkUploadReadDto getById(Long id) {
        log.debug("Getting BulkUpload by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("BulkUpload", id));
    }

    public BulkUploadReadDto create(BulkUploadCreateDto createDto) {
        log.info("Creating new BulkUpload: {}", createDto);
        BulkUpload entity = convertToEntity(createDto);
        BulkUpload saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public BulkUploadReadDto update(Long id, BulkUploadUpdateDto updateDto) {
        log.info("Updating BulkUpload with id: {}", id);
        BulkUpload existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BulkUpload", id));
        
        updateEntityFromDto(existing, updateDto);
        BulkUpload updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting BulkUpload with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("BulkUpload", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<BulkUploadReadDto> findAll(Pageable pageable) {
        log.debug("Finding all BulkUpload with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private BulkUploadReadDto convertToReadDto(BulkUpload entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new BulkUploadReadDto();
    }

    private BulkUpload convertToEntity(BulkUploadCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new BulkUpload();
    }

    private void updateEntityFromDto(BulkUpload entity, BulkUploadUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public BulkUploadReadDto suspendBulkUpload(Long id) {
        log.info("Suspending BulkUpload with id: {}", id);
        BulkUpload entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BulkUpload", id));
        entity.suspend();
        BulkUpload updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public BulkUploadReadDto activateBulkUpload(Long id) {
        log.info("Activating BulkUpload with id: {}", id);
        BulkUpload entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BulkUpload", id));
        entity.activate();
        BulkUpload updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
