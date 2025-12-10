package com.example.blog.service;

import com.example.blog.entity.DocumentPreview;
import com.example.blog.repository.DocumentPreviewRepository;
import com.example.blog.dto.DocumentPreviewCreateDto;
import com.example.blog.dto.DocumentPreviewReadDto;
import com.example.blog.dto.DocumentPreviewUpdateDto;
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

import com.example.blog.enums.DocumentPreviewStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DocumentPreviewService {

    private final DocumentPreviewRepository repository;

    @Transactional(readOnly = true)
    public List<DocumentPreviewReadDto> findAll() {
        log.debug("Finding all DocumentPreviews");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DocumentPreviewReadDto> findById(Long id) {
        log.debug("Finding DocumentPreview by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public DocumentPreviewReadDto getById(Long id) {
        log.debug("Getting DocumentPreview by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("DocumentPreview", id));
    }

    public DocumentPreviewReadDto create(DocumentPreviewCreateDto createDto) {
        log.info("Creating new DocumentPreview: {}", createDto);
        DocumentPreview entity = convertToEntity(createDto);
        DocumentPreview saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public DocumentPreviewReadDto update(Long id, DocumentPreviewUpdateDto updateDto) {
        log.info("Updating DocumentPreview with id: {}", id);
        DocumentPreview existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DocumentPreview", id));
        
        updateEntityFromDto(existing, updateDto);
        DocumentPreview updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting DocumentPreview with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("DocumentPreview", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<DocumentPreviewReadDto> findAll(Pageable pageable) {
        log.debug("Finding all DocumentPreview with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private DocumentPreviewReadDto convertToReadDto(DocumentPreview entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new DocumentPreviewReadDto();
    }

    private DocumentPreview convertToEntity(DocumentPreviewCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new DocumentPreview();
    }

    private void updateEntityFromDto(DocumentPreview entity, DocumentPreviewUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public DocumentPreviewReadDto suspendDocumentPreview(Long id) {
        log.info("Suspending DocumentPreview with id: {}", id);
        DocumentPreview entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DocumentPreview", id));
        entity.suspend();
        DocumentPreview updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public DocumentPreviewReadDto activateDocumentPreview(Long id) {
        log.info("Activating DocumentPreview with id: {}", id);
        DocumentPreview entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DocumentPreview", id));
        entity.activate();
        DocumentPreview updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
