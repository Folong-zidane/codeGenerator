package com.example.blog.service;

import com.example.blog.entity.Archive;
import com.example.blog.repository.ArchiveRepository;
import com.example.blog.dto.ArchiveCreateDto;
import com.example.blog.dto.ArchiveReadDto;
import com.example.blog.dto.ArchiveUpdateDto;
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

import com.example.blog.enums.ArchiveStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArchiveService {

    private final ArchiveRepository repository;

    @Transactional(readOnly = true)
    public List<ArchiveReadDto> findAll() {
        log.debug("Finding all Archives");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ArchiveReadDto> findById(Long id) {
        log.debug("Finding Archive by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ArchiveReadDto getById(Long id) {
        log.debug("Getting Archive by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Archive", id));
    }

    public ArchiveReadDto create(ArchiveCreateDto createDto) {
        log.info("Creating new Archive: {}", createDto);
        Archive entity = convertToEntity(createDto);
        Archive saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ArchiveReadDto update(Long id, ArchiveUpdateDto updateDto) {
        log.info("Updating Archive with id: {}", id);
        Archive existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Archive", id));
        
        updateEntityFromDto(existing, updateDto);
        Archive updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Archive with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Archive", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ArchiveReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Archive with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ArchiveReadDto convertToReadDto(Archive entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ArchiveReadDto();
    }

    private Archive convertToEntity(ArchiveCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Archive();
    }

    private void updateEntityFromDto(Archive entity, ArchiveUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ArchiveReadDto suspendArchive(Long id) {
        log.info("Suspending Archive with id: {}", id);
        Archive entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Archive", id));
        entity.suspend();
        Archive updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ArchiveReadDto activateArchive(Long id) {
        log.info("Activating Archive with id: {}", id);
        Archive entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Archive", id));
        entity.activate();
        Archive updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
