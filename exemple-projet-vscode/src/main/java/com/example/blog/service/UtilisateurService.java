package com.example.blog.service;

import com.example.blog.entity.Utilisateur;
import com.example.blog.repository.UtilisateurRepository;
import com.example.blog.dto.UtilisateurCreateDto;
import com.example.blog.dto.UtilisateurReadDto;
import com.example.blog.dto.UtilisateurUpdateDto;
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

import com.example.blog.enums.UtilisateurStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UtilisateurService {

    private final UtilisateurRepository repository;

    @Transactional(readOnly = true)
    public List<UtilisateurReadDto> findAll() {
        log.debug("Finding all Utilisateurs");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UtilisateurReadDto> findById(Long id) {
        log.debug("Finding Utilisateur by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public UtilisateurReadDto getById(Long id) {
        log.debug("Getting Utilisateur by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur", id));
    }

    public UtilisateurReadDto create(UtilisateurCreateDto createDto) {
        log.info("Creating new Utilisateur: {}", createDto);
        Utilisateur entity = convertToEntity(createDto);
        Utilisateur saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public UtilisateurReadDto update(Long id, UtilisateurUpdateDto updateDto) {
        log.info("Updating Utilisateur with id: {}", id);
        Utilisateur existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur", id));
        
        updateEntityFromDto(existing, updateDto);
        Utilisateur updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Utilisateur with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UtilisateurReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Utilisateur with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private UtilisateurReadDto convertToReadDto(Utilisateur entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new UtilisateurReadDto();
    }

    private Utilisateur convertToEntity(UtilisateurCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Utilisateur();
    }

    private void updateEntityFromDto(Utilisateur entity, UtilisateurUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public UtilisateurReadDto suspendUtilisateur(Long id) {
        log.info("Suspending Utilisateur with id: {}", id);
        Utilisateur entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur", id));
        entity.suspend();
        Utilisateur updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public UtilisateurReadDto activateUtilisateur(Long id) {
        log.info("Activating Utilisateur with id: {}", id);
        Utilisateur entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur", id));
        entity.activate();
        Utilisateur updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
