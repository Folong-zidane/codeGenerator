package com.example.blog.service;

import com.example.blog.entity.Rubrique;
import com.example.blog.repository.RubriqueRepository;
import com.example.blog.dto.RubriqueCreateDto;
import com.example.blog.dto.RubriqueReadDto;
import com.example.blog.dto.RubriqueUpdateDto;
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

import com.example.blog.enums.RubriqueStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RubriqueService {

    private final RubriqueRepository repository;

    @Transactional(readOnly = true)
    public List<RubriqueReadDto> findAll() {
        log.debug("Finding all Rubriques");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RubriqueReadDto> findById(Long id) {
        log.debug("Finding Rubrique by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public RubriqueReadDto getById(Long id) {
        log.debug("Getting Rubrique by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Rubrique", id));
    }

    public RubriqueReadDto create(RubriqueCreateDto createDto) {
        log.info("Creating new Rubrique: {}", createDto);
        Rubrique entity = convertToEntity(createDto);
        Rubrique saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public RubriqueReadDto update(Long id, RubriqueUpdateDto updateDto) {
        log.info("Updating Rubrique with id: {}", id);
        Rubrique existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rubrique", id));
        
        updateEntityFromDto(existing, updateDto);
        Rubrique updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Rubrique with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Rubrique", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<RubriqueReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Rubrique with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private RubriqueReadDto convertToReadDto(Rubrique entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new RubriqueReadDto();
    }

    private Rubrique convertToEntity(RubriqueCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Rubrique();
    }

    private void updateEntityFromDto(Rubrique entity, RubriqueUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public RubriqueReadDto suspendRubrique(Long id) {
        log.info("Suspending Rubrique with id: {}", id);
        Rubrique entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rubrique", id));
        entity.suspend();
        Rubrique updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public RubriqueReadDto activateRubrique(Long id) {
        log.info("Activating Rubrique with id: {}", id);
        Rubrique entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rubrique", id));
        entity.activate();
        Rubrique updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
