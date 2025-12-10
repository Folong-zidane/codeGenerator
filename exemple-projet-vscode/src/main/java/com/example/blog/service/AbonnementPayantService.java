package com.example.blog.service;

import com.example.blog.entity.AbonnementPayant;
import com.example.blog.repository.AbonnementPayantRepository;
import com.example.blog.dto.AbonnementPayantCreateDto;
import com.example.blog.dto.AbonnementPayantReadDto;
import com.example.blog.dto.AbonnementPayantUpdateDto;
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

import com.example.blog.enums.AbonnementPayantStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AbonnementPayantService {

    private final AbonnementPayantRepository repository;

    @Transactional(readOnly = true)
    public List<AbonnementPayantReadDto> findAll() {
        log.debug("Finding all AbonnementPayants");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AbonnementPayantReadDto> findById(Long id) {
        log.debug("Finding AbonnementPayant by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public AbonnementPayantReadDto getById(Long id) {
        log.debug("Getting AbonnementPayant by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementPayant", id));
    }

    public AbonnementPayantReadDto create(AbonnementPayantCreateDto createDto) {
        log.info("Creating new AbonnementPayant: {}", createDto);
        AbonnementPayant entity = convertToEntity(createDto);
        AbonnementPayant saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public AbonnementPayantReadDto update(Long id, AbonnementPayantUpdateDto updateDto) {
        log.info("Updating AbonnementPayant with id: {}", id);
        AbonnementPayant existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementPayant", id));
        
        updateEntityFromDto(existing, updateDto);
        AbonnementPayant updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting AbonnementPayant with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("AbonnementPayant", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AbonnementPayantReadDto> findAll(Pageable pageable) {
        log.debug("Finding all AbonnementPayant with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private AbonnementPayantReadDto convertToReadDto(AbonnementPayant entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new AbonnementPayantReadDto();
    }

    private AbonnementPayant convertToEntity(AbonnementPayantCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new AbonnementPayant();
    }

    private void updateEntityFromDto(AbonnementPayant entity, AbonnementPayantUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public AbonnementPayantReadDto suspendAbonnementPayant(Long id) {
        log.info("Suspending AbonnementPayant with id: {}", id);
        AbonnementPayant entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementPayant", id));
        entity.suspend();
        AbonnementPayant updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public AbonnementPayantReadDto activateAbonnementPayant(Long id) {
        log.info("Activating AbonnementPayant with id: {}", id);
        AbonnementPayant entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementPayant", id));
        entity.activate();
        AbonnementPayant updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
