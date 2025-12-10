package com.example.blog.service;

import com.example.blog.entity.LangueContenu;
import com.example.blog.repository.LangueContenuRepository;
import com.example.blog.dto.LangueContenuCreateDto;
import com.example.blog.dto.LangueContenuReadDto;
import com.example.blog.dto.LangueContenuUpdateDto;
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

import com.example.blog.enums.LangueContenuStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LangueContenuService {

    private final LangueContenuRepository repository;

    @Transactional(readOnly = true)
    public List<LangueContenuReadDto> findAll() {
        log.debug("Finding all LangueContenus");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<LangueContenuReadDto> findById(Long id) {
        log.debug("Finding LangueContenu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public LangueContenuReadDto getById(Long id) {
        log.debug("Getting LangueContenu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("LangueContenu", id));
    }

    public LangueContenuReadDto create(LangueContenuCreateDto createDto) {
        log.info("Creating new LangueContenu: {}", createDto);
        LangueContenu entity = convertToEntity(createDto);
        LangueContenu saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public LangueContenuReadDto update(Long id, LangueContenuUpdateDto updateDto) {
        log.info("Updating LangueContenu with id: {}", id);
        LangueContenu existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LangueContenu", id));
        
        updateEntityFromDto(existing, updateDto);
        LangueContenu updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting LangueContenu with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("LangueContenu", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<LangueContenuReadDto> findAll(Pageable pageable) {
        log.debug("Finding all LangueContenu with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private LangueContenuReadDto convertToReadDto(LangueContenu entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new LangueContenuReadDto();
    }

    private LangueContenu convertToEntity(LangueContenuCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new LangueContenu();
    }

    private void updateEntityFromDto(LangueContenu entity, LangueContenuUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public LangueContenuReadDto suspendLangueContenu(Long id) {
        log.info("Suspending LangueContenu with id: {}", id);
        LangueContenu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LangueContenu", id));
        entity.suspend();
        LangueContenu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public LangueContenuReadDto activateLangueContenu(Long id) {
        log.info("Activating LangueContenu with id: {}", id);
        LangueContenu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("LangueContenu", id));
        entity.activate();
        LangueContenu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
