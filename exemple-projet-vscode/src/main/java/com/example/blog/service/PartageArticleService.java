package com.example.blog.service;

import com.example.blog.entity.PartageArticle;
import com.example.blog.repository.PartageArticleRepository;
import com.example.blog.dto.PartageArticleCreateDto;
import com.example.blog.dto.PartageArticleReadDto;
import com.example.blog.dto.PartageArticleUpdateDto;
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

import com.example.blog.enums.PartageArticleStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PartageArticleService {

    private final PartageArticleRepository repository;

    @Transactional(readOnly = true)
    public List<PartageArticleReadDto> findAll() {
        log.debug("Finding all PartageArticles");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PartageArticleReadDto> findById(Long id) {
        log.debug("Finding PartageArticle by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PartageArticleReadDto getById(Long id) {
        log.debug("Getting PartageArticle by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("PartageArticle", id));
    }

    public PartageArticleReadDto create(PartageArticleCreateDto createDto) {
        log.info("Creating new PartageArticle: {}", createDto);
        PartageArticle entity = convertToEntity(createDto);
        PartageArticle saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PartageArticleReadDto update(Long id, PartageArticleUpdateDto updateDto) {
        log.info("Updating PartageArticle with id: {}", id);
        PartageArticle existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageArticle", id));
        
        updateEntityFromDto(existing, updateDto);
        PartageArticle updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting PartageArticle with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PartageArticle", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PartageArticleReadDto> findAll(Pageable pageable) {
        log.debug("Finding all PartageArticle with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PartageArticleReadDto convertToReadDto(PartageArticle entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PartageArticleReadDto();
    }

    private PartageArticle convertToEntity(PartageArticleCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new PartageArticle();
    }

    private void updateEntityFromDto(PartageArticle entity, PartageArticleUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PartageArticleReadDto suspendPartageArticle(Long id) {
        log.info("Suspending PartageArticle with id: {}", id);
        PartageArticle entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageArticle", id));
        entity.suspend();
        PartageArticle updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PartageArticleReadDto activatePartageArticle(Long id) {
        log.info("Activating PartageArticle with id: {}", id);
        PartageArticle entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageArticle", id));
        entity.activate();
        PartageArticle updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
