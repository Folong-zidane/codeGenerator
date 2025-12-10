package com.example.blog.service;

import com.example.blog.entity.Statistiques;
import com.example.blog.repository.StatistiquesRepository;
import com.example.blog.dto.StatistiquesCreateDto;
import com.example.blog.dto.StatistiquesReadDto;
import com.example.blog.dto.StatistiquesUpdateDto;
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

import com.example.blog.enums.StatistiquesStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatistiquesService {

    private final StatistiquesRepository repository;

    @Transactional(readOnly = true)
    public List<StatistiquesReadDto> findAll() {
        log.debug("Finding all Statistiquess");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<StatistiquesReadDto> findById(Long id) {
        log.debug("Finding Statistiques by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public StatistiquesReadDto getById(Long id) {
        log.debug("Getting Statistiques by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Statistiques", id));
    }

    public StatistiquesReadDto create(StatistiquesCreateDto createDto) {
        log.info("Creating new Statistiques: {}", createDto);
        Statistiques entity = convertToEntity(createDto);
        Statistiques saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public StatistiquesReadDto update(Long id, StatistiquesUpdateDto updateDto) {
        log.info("Updating Statistiques with id: {}", id);
        Statistiques existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Statistiques", id));
        
        updateEntityFromDto(existing, updateDto);
        Statistiques updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Statistiques with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Statistiques", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<StatistiquesReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Statistiques with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private StatistiquesReadDto convertToReadDto(Statistiques entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new StatistiquesReadDto();
    }

    private Statistiques convertToEntity(StatistiquesCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Statistiques();
    }

    private void updateEntityFromDto(Statistiques entity, StatistiquesUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public StatistiquesReadDto suspendStatistiques(Long id) {
        log.info("Suspending Statistiques with id: {}", id);
        Statistiques entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Statistiques", id));
        entity.suspend();
        Statistiques updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public StatistiquesReadDto activateStatistiques(Long id) {
        log.info("Activating Statistiques with id: {}", id);
        Statistiques entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Statistiques", id));
        entity.activate();
        Statistiques updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
