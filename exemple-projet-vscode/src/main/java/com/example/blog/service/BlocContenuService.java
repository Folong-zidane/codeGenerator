package com.example.blog.service;

import com.example.blog.entity.BlocContenu;
import com.example.blog.repository.BlocContenuRepository;
import com.example.blog.dto.BlocContenuCreateDto;
import com.example.blog.dto.BlocContenuReadDto;
import com.example.blog.dto.BlocContenuUpdateDto;
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

import com.example.blog.enums.BlocContenuStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlocContenuService {

    private final BlocContenuRepository repository;

    @Transactional(readOnly = true)
    public List<BlocContenuReadDto> findAll() {
        log.debug("Finding all BlocContenus");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BlocContenuReadDto> findById(Long id) {
        log.debug("Finding BlocContenu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public BlocContenuReadDto getById(Long id) {
        log.debug("Getting BlocContenu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("BlocContenu", id));
    }

    public BlocContenuReadDto create(BlocContenuCreateDto createDto) {
        log.info("Creating new BlocContenu: {}", createDto);
        BlocContenu entity = convertToEntity(createDto);
        BlocContenu saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public BlocContenuReadDto update(Long id, BlocContenuUpdateDto updateDto) {
        log.info("Updating BlocContenu with id: {}", id);
        BlocContenu existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlocContenu", id));
        
        updateEntityFromDto(existing, updateDto);
        BlocContenu updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting BlocContenu with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("BlocContenu", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<BlocContenuReadDto> findAll(Pageable pageable) {
        log.debug("Finding all BlocContenu with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private BlocContenuReadDto convertToReadDto(BlocContenu entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new BlocContenuReadDto();
    }

    private BlocContenu convertToEntity(BlocContenuCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new BlocContenu();
    }

    private void updateEntityFromDto(BlocContenu entity, BlocContenuUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public BlocContenuReadDto suspendBlocContenu(Long id) {
        log.info("Suspending BlocContenu with id: {}", id);
        BlocContenu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlocContenu", id));
        entity.suspend();
        BlocContenu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public BlocContenuReadDto activateBlocContenu(Long id) {
        log.info("Activating BlocContenu with id: {}", id);
        BlocContenu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlocContenu", id));
        entity.activate();
        BlocContenu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
