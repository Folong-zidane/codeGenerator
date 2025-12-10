package com.example.blog.service;

import com.example.blog.entity.Commentaire;
import com.example.blog.repository.CommentaireRepository;
import com.example.blog.dto.CommentaireCreateDto;
import com.example.blog.dto.CommentaireReadDto;
import com.example.blog.dto.CommentaireUpdateDto;
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

import com.example.blog.enums.CommentaireStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentaireService {

    private final CommentaireRepository repository;

    @Transactional(readOnly = true)
    public List<CommentaireReadDto> findAll() {
        log.debug("Finding all Commentaires");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CommentaireReadDto> findById(Long id) {
        log.debug("Finding Commentaire by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CommentaireReadDto getById(Long id) {
        log.debug("Getting Commentaire by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Commentaire", id));
    }

    public CommentaireReadDto create(CommentaireCreateDto createDto) {
        log.info("Creating new Commentaire: {}", createDto);
        Commentaire entity = convertToEntity(createDto);
        Commentaire saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CommentaireReadDto update(Long id, CommentaireUpdateDto updateDto) {
        log.info("Updating Commentaire with id: {}", id);
        Commentaire existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Commentaire", id));
        
        updateEntityFromDto(existing, updateDto);
        Commentaire updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Commentaire with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Commentaire", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CommentaireReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Commentaire with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CommentaireReadDto convertToReadDto(Commentaire entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CommentaireReadDto();
    }

    private Commentaire convertToEntity(CommentaireCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Commentaire();
    }

    private void updateEntityFromDto(Commentaire entity, CommentaireUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CommentaireReadDto suspendCommentaire(Long id) {
        log.info("Suspending Commentaire with id: {}", id);
        Commentaire entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Commentaire", id));
        entity.suspend();
        Commentaire updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CommentaireReadDto activateCommentaire(Long id) {
        log.info("Activating Commentaire with id: {}", id);
        Commentaire entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Commentaire", id));
        entity.activate();
        Commentaire updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
