package com.example.blog.service;

import com.example.blog.entity.Langue;
import com.example.blog.repository.LangueRepository;
import com.example.blog.dto.LangueCreateDto;
import com.example.blog.dto.LangueReadDto;
import com.example.blog.dto.LangueUpdateDto;
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

import com.example.blog.enums.LangueStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LangueService {

    private final LangueRepository repository;

    @Transactional(readOnly = true)
    public List<LangueReadDto> findAll() {
        log.debug("Finding all Langues");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<LangueReadDto> findById(Long id) {
        log.debug("Finding Langue by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public LangueReadDto getById(Long id) {
        log.debug("Getting Langue by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Langue", id));
    }

    public LangueReadDto create(LangueCreateDto createDto) {
        log.info("Creating new Langue: {}", createDto);
        Langue entity = convertToEntity(createDto);
        Langue saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public LangueReadDto update(Long id, LangueUpdateDto updateDto) {
        log.info("Updating Langue with id: {}", id);
        Langue existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Langue", id));
        
        updateEntityFromDto(existing, updateDto);
        Langue updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Langue with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Langue", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<LangueReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Langue with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private LangueReadDto convertToReadDto(Langue entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new LangueReadDto();
    }

    private Langue convertToEntity(LangueCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Langue();
    }

    private void updateEntityFromDto(Langue entity, LangueUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public LangueReadDto suspendLangue(Long id) {
        log.info("Suspending Langue with id: {}", id);
        Langue entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Langue", id));
        entity.suspend();
        Langue updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public LangueReadDto activateLangue(Long id) {
        log.info("Activating Langue with id: {}", id);
        Langue entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Langue", id));
        entity.activate();
        Langue updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
