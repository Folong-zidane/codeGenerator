package com.example.blog.service;

import com.example.blog.entity.Favori;
import com.example.blog.repository.FavoriRepository;
import com.example.blog.dto.FavoriCreateDto;
import com.example.blog.dto.FavoriReadDto;
import com.example.blog.dto.FavoriUpdateDto;
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

import com.example.blog.enums.FavoriStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FavoriService {

    private final FavoriRepository repository;

    @Transactional(readOnly = true)
    public List<FavoriReadDto> findAll() {
        log.debug("Finding all Favoris");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<FavoriReadDto> findById(Long id) {
        log.debug("Finding Favori by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public FavoriReadDto getById(Long id) {
        log.debug("Getting Favori by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Favori", id));
    }

    public FavoriReadDto create(FavoriCreateDto createDto) {
        log.info("Creating new Favori: {}", createDto);
        Favori entity = convertToEntity(createDto);
        Favori saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public FavoriReadDto update(Long id, FavoriUpdateDto updateDto) {
        log.info("Updating Favori with id: {}", id);
        Favori existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Favori", id));
        
        updateEntityFromDto(existing, updateDto);
        Favori updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Favori with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Favori", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<FavoriReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Favori with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private FavoriReadDto convertToReadDto(Favori entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new FavoriReadDto();
    }

    private Favori convertToEntity(FavoriCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Favori();
    }

    private void updateEntityFromDto(Favori entity, FavoriUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public FavoriReadDto suspendFavori(Long id) {
        log.info("Suspending Favori with id: {}", id);
        Favori entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Favori", id));
        entity.suspend();
        Favori updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public FavoriReadDto activateFavori(Long id) {
        log.info("Activating Favori with id: {}", id);
        Favori entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Favori", id));
        entity.activate();
        Favori updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
