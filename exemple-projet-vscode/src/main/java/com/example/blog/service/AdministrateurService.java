package com.example.blog.service;

import com.example.blog.entity.Administrateur;
import com.example.blog.repository.AdministrateurRepository;
import com.example.blog.dto.AdministrateurCreateDto;
import com.example.blog.dto.AdministrateurReadDto;
import com.example.blog.dto.AdministrateurUpdateDto;
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

import com.example.blog.enums.AdministrateurStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdministrateurService {

    private final AdministrateurRepository repository;

    @Transactional(readOnly = true)
    public List<AdministrateurReadDto> findAll() {
        log.debug("Finding all Administrateurs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AdministrateurReadDto> findById(Long id) {
        log.debug("Finding Administrateur by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public AdministrateurReadDto getById(Long id) {
        log.debug("Getting Administrateur by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Administrateur", id));
    }

    public AdministrateurReadDto create(AdministrateurCreateDto createDto) {
        log.info("Creating new Administrateur: {}", createDto);
        Administrateur entity = convertToEntity(createDto);
        Administrateur saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public AdministrateurReadDto update(Long id, AdministrateurUpdateDto updateDto) {
        log.info("Updating Administrateur with id: {}", id);
        Administrateur existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Administrateur", id));
        
        updateEntityFromDto(existing, updateDto);
        Administrateur updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Administrateur with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Administrateur", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AdministrateurReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Administrateur with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private AdministrateurReadDto convertToReadDto(Administrateur entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new AdministrateurReadDto();
    }

    private Administrateur convertToEntity(AdministrateurCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Administrateur();
    }

    private void updateEntityFromDto(Administrateur entity, AdministrateurUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public AdministrateurReadDto suspendAdministrateur(Long id) {
        log.info("Suspending Administrateur with id: {}", id);
        Administrateur entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Administrateur", id));
        entity.suspend();
        Administrateur updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public AdministrateurReadDto activateAdministrateur(Long id) {
        log.info("Activating Administrateur with id: {}", id);
        Administrateur entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Administrateur", id));
        entity.activate();
        Administrateur updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
