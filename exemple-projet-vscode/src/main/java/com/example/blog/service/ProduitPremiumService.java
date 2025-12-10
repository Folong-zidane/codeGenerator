package com.example.blog.service;

import com.example.blog.entity.ProduitPremium;
import com.example.blog.repository.ProduitPremiumRepository;
import com.example.blog.dto.ProduitPremiumCreateDto;
import com.example.blog.dto.ProduitPremiumReadDto;
import com.example.blog.dto.ProduitPremiumUpdateDto;
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

import com.example.blog.enums.ProduitPremiumStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProduitPremiumService {

    private final ProduitPremiumRepository repository;

    @Transactional(readOnly = true)
    public List<ProduitPremiumReadDto> findAll() {
        log.debug("Finding all ProduitPremiums");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProduitPremiumReadDto> findById(Long id) {
        log.debug("Finding ProduitPremium by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ProduitPremiumReadDto getById(Long id) {
        log.debug("Getting ProduitPremium by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("ProduitPremium", id));
    }

    public ProduitPremiumReadDto create(ProduitPremiumCreateDto createDto) {
        log.info("Creating new ProduitPremium: {}", createDto);
        ProduitPremium entity = convertToEntity(createDto);
        ProduitPremium saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ProduitPremiumReadDto update(Long id, ProduitPremiumUpdateDto updateDto) {
        log.info("Updating ProduitPremium with id: {}", id);
        ProduitPremium existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProduitPremium", id));
        
        updateEntityFromDto(existing, updateDto);
        ProduitPremium updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting ProduitPremium with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ProduitPremium", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProduitPremiumReadDto> findAll(Pageable pageable) {
        log.debug("Finding all ProduitPremium with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ProduitPremiumReadDto convertToReadDto(ProduitPremium entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ProduitPremiumReadDto();
    }

    private ProduitPremium convertToEntity(ProduitPremiumCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new ProduitPremium();
    }

    private void updateEntityFromDto(ProduitPremium entity, ProduitPremiumUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ProduitPremiumReadDto suspendProduitPremium(Long id) {
        log.info("Suspending ProduitPremium with id: {}", id);
        ProduitPremium entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProduitPremium", id));
        entity.suspend();
        ProduitPremium updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ProduitPremiumReadDto activateProduitPremium(Long id) {
        log.info("Activating ProduitPremium with id: {}", id);
        ProduitPremium entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProduitPremium", id));
        entity.activate();
        ProduitPremium updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
