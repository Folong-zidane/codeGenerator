package com.example.blog.service;

import com.example.blog.entity.Traduction;
import com.example.blog.repository.TraductionRepository;
import com.example.blog.dto.TraductionCreateDto;
import com.example.blog.dto.TraductionReadDto;
import com.example.blog.dto.TraductionUpdateDto;
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

import com.example.blog.enums.TraductionStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TraductionService {

    private final TraductionRepository repository;

    @Transactional(readOnly = true)
    public List<TraductionReadDto> findAll() {
        log.debug("Finding all Traductions");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TraductionReadDto> findById(Long id) {
        log.debug("Finding Traduction by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public TraductionReadDto getById(Long id) {
        log.debug("Getting Traduction by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Traduction", id));
    }

    public TraductionReadDto create(TraductionCreateDto createDto) {
        log.info("Creating new Traduction: {}", createDto);
        Traduction entity = convertToEntity(createDto);
        Traduction saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public TraductionReadDto update(Long id, TraductionUpdateDto updateDto) {
        log.info("Updating Traduction with id: {}", id);
        Traduction existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Traduction", id));
        
        updateEntityFromDto(existing, updateDto);
        Traduction updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Traduction with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Traduction", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TraductionReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Traduction with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private TraductionReadDto convertToReadDto(Traduction entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new TraductionReadDto();
    }

    private Traduction convertToEntity(TraductionCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Traduction();
    }

    private void updateEntityFromDto(Traduction entity, TraductionUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public TraductionReadDto suspendTraduction(Long id) {
        log.info("Suspending Traduction with id: {}", id);
        Traduction entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Traduction", id));
        entity.suspend();
        Traduction updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public TraductionReadDto activateTraduction(Long id) {
        log.info("Activating Traduction with id: {}", id);
        Traduction entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Traduction", id));
        entity.activate();
        Traduction updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
