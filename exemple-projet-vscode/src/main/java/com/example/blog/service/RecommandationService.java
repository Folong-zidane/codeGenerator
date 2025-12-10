package com.example.blog.service;

import com.example.blog.entity.Recommandation;
import com.example.blog.repository.RecommandationRepository;
import com.example.blog.dto.RecommandationCreateDto;
import com.example.blog.dto.RecommandationReadDto;
import com.example.blog.dto.RecommandationUpdateDto;
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

import com.example.blog.enums.RecommandationStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommandationService {

    private final RecommandationRepository repository;

    @Transactional(readOnly = true)
    public List<RecommandationReadDto> findAll() {
        log.debug("Finding all Recommandations");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RecommandationReadDto> findById(Long id) {
        log.debug("Finding Recommandation by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public RecommandationReadDto getById(Long id) {
        log.debug("Getting Recommandation by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Recommandation", id));
    }

    public RecommandationReadDto create(RecommandationCreateDto createDto) {
        log.info("Creating new Recommandation: {}", createDto);
        Recommandation entity = convertToEntity(createDto);
        Recommandation saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public RecommandationReadDto update(Long id, RecommandationUpdateDto updateDto) {
        log.info("Updating Recommandation with id: {}", id);
        Recommandation existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Recommandation", id));
        
        updateEntityFromDto(existing, updateDto);
        Recommandation updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Recommandation with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Recommandation", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<RecommandationReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Recommandation with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private RecommandationReadDto convertToReadDto(Recommandation entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new RecommandationReadDto();
    }

    private Recommandation convertToEntity(RecommandationCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Recommandation();
    }

    private void updateEntityFromDto(Recommandation entity, RecommandationUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public RecommandationReadDto suspendRecommandation(Long id) {
        log.info("Suspending Recommandation with id: {}", id);
        Recommandation entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Recommandation", id));
        entity.suspend();
        Recommandation updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public RecommandationReadDto activateRecommandation(Long id) {
        log.info("Activating Recommandation with id: {}", id);
        Recommandation entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Recommandation", id));
        entity.activate();
        Recommandation updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
