package com.example.blog.service;

import com.example.blog.entity.Paiement;
import com.example.blog.repository.PaiementRepository;
import com.example.blog.dto.PaiementCreateDto;
import com.example.blog.dto.PaiementReadDto;
import com.example.blog.dto.PaiementUpdateDto;
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

import com.example.blog.enums.PaiementStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaiementService {

    private final PaiementRepository repository;

    @Transactional(readOnly = true)
    public List<PaiementReadDto> findAll() {
        log.debug("Finding all Paiements");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PaiementReadDto> findById(Long id) {
        log.debug("Finding Paiement by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PaiementReadDto getById(Long id) {
        log.debug("Getting Paiement by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Paiement", id));
    }

    public PaiementReadDto create(PaiementCreateDto createDto) {
        log.info("Creating new Paiement: {}", createDto);
        Paiement entity = convertToEntity(createDto);
        Paiement saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PaiementReadDto update(Long id, PaiementUpdateDto updateDto) {
        log.info("Updating Paiement with id: {}", id);
        Paiement existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paiement", id));
        
        updateEntityFromDto(existing, updateDto);
        Paiement updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Paiement with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Paiement", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PaiementReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Paiement with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PaiementReadDto convertToReadDto(Paiement entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PaiementReadDto();
    }

    private Paiement convertToEntity(PaiementCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Paiement();
    }

    private void updateEntityFromDto(Paiement entity, PaiementUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PaiementReadDto suspendPaiement(Long id) {
        log.info("Suspending Paiement with id: {}", id);
        Paiement entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paiement", id));
        entity.suspend();
        Paiement updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PaiementReadDto activatePaiement(Long id) {
        log.info("Activating Paiement with id: {}", id);
        Paiement entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paiement", id));
        entity.activate();
        Paiement updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
