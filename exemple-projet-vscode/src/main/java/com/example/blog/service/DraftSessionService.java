package com.example.blog.service;

import com.example.blog.entity.DraftSession;
import com.example.blog.repository.DraftSessionRepository;
import com.example.blog.dto.DraftSessionCreateDto;
import com.example.blog.dto.DraftSessionReadDto;
import com.example.blog.dto.DraftSessionUpdateDto;
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

import com.example.blog.enums.DraftSessionStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DraftSessionService {

    private final DraftSessionRepository repository;

    @Transactional(readOnly = true)
    public List<DraftSessionReadDto> findAll() {
        log.debug("Finding all DraftSessions");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DraftSessionReadDto> findById(Long id) {
        log.debug("Finding DraftSession by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public DraftSessionReadDto getById(Long id) {
        log.debug("Getting DraftSession by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("DraftSession", id));
    }

    public DraftSessionReadDto create(DraftSessionCreateDto createDto) {
        log.info("Creating new DraftSession: {}", createDto);
        DraftSession entity = convertToEntity(createDto);
        DraftSession saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public DraftSessionReadDto update(Long id, DraftSessionUpdateDto updateDto) {
        log.info("Updating DraftSession with id: {}", id);
        DraftSession existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DraftSession", id));
        
        updateEntityFromDto(existing, updateDto);
        DraftSession updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting DraftSession with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("DraftSession", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<DraftSessionReadDto> findAll(Pageable pageable) {
        log.debug("Finding all DraftSession with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private DraftSessionReadDto convertToReadDto(DraftSession entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new DraftSessionReadDto();
    }

    private DraftSession convertToEntity(DraftSessionCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new DraftSession();
    }

    private void updateEntityFromDto(DraftSession entity, DraftSessionUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public DraftSessionReadDto suspendDraftSession(Long id) {
        log.info("Suspending DraftSession with id: {}", id);
        DraftSession entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DraftSession", id));
        entity.suspend();
        DraftSession updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public DraftSessionReadDto activateDraftSession(Long id) {
        log.info("Activating DraftSession with id: {}", id);
        DraftSession entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DraftSession", id));
        entity.activate();
        DraftSession updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
