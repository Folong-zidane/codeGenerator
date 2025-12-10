package com.example.blog.service;

import com.example.blog.entity.Panier;
import com.example.blog.repository.PanierRepository;
import com.example.blog.dto.PanierCreateDto;
import com.example.blog.dto.PanierReadDto;
import com.example.blog.dto.PanierUpdateDto;
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

import com.example.blog.enums.PanierStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PanierService {

    private final PanierRepository repository;

    @Transactional(readOnly = true)
    public List<PanierReadDto> findAll() {
        log.debug("Finding all Paniers");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PanierReadDto> findById(Long id) {
        log.debug("Finding Panier by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PanierReadDto getById(Long id) {
        log.debug("Getting Panier by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Panier", id));
    }

    public PanierReadDto create(PanierCreateDto createDto) {
        log.info("Creating new Panier: {}", createDto);
        Panier entity = convertToEntity(createDto);
        Panier saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PanierReadDto update(Long id, PanierUpdateDto updateDto) {
        log.info("Updating Panier with id: {}", id);
        Panier existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Panier", id));
        
        updateEntityFromDto(existing, updateDto);
        Panier updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Panier with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Panier", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PanierReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Panier with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PanierReadDto convertToReadDto(Panier entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PanierReadDto();
    }

    private Panier convertToEntity(PanierCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Panier();
    }

    private void updateEntityFromDto(Panier entity, PanierUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PanierReadDto suspendPanier(Long id) {
        log.info("Suspending Panier with id: {}", id);
        Panier entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Panier", id));
        entity.suspend();
        Panier updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PanierReadDto activatePanier(Long id) {
        log.info("Activating Panier with id: {}", id);
        Panier entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Panier", id));
        entity.activate();
        Panier updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
