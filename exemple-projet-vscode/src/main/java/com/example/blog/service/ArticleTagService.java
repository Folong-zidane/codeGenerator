package com.example.blog.service;

import com.example.blog.entity.ArticleTag;
import com.example.blog.repository.ArticleTagRepository;
import com.example.blog.dto.ArticleTagCreateDto;
import com.example.blog.dto.ArticleTagReadDto;
import com.example.blog.dto.ArticleTagUpdateDto;
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

import com.example.blog.enums.ArticleTagStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleTagService {

    private final ArticleTagRepository repository;

    @Transactional(readOnly = true)
    public List<ArticleTagReadDto> findAll() {
        log.debug("Finding all ArticleTags");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ArticleTagReadDto> findById(Long id) {
        log.debug("Finding ArticleTag by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ArticleTagReadDto getById(Long id) {
        log.debug("Getting ArticleTag by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("ArticleTag", id));
    }

    public ArticleTagReadDto create(ArticleTagCreateDto createDto) {
        log.info("Creating new ArticleTag: {}", createDto);
        ArticleTag entity = convertToEntity(createDto);
        ArticleTag saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ArticleTagReadDto update(Long id, ArticleTagUpdateDto updateDto) {
        log.info("Updating ArticleTag with id: {}", id);
        ArticleTag existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ArticleTag", id));
        
        updateEntityFromDto(existing, updateDto);
        ArticleTag updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting ArticleTag with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ArticleTag", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ArticleTagReadDto> findAll(Pageable pageable) {
        log.debug("Finding all ArticleTag with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ArticleTagReadDto convertToReadDto(ArticleTag entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ArticleTagReadDto();
    }

    private ArticleTag convertToEntity(ArticleTagCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new ArticleTag();
    }

    private void updateEntityFromDto(ArticleTag entity, ArticleTagUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ArticleTagReadDto suspendArticleTag(Long id) {
        log.info("Suspending ArticleTag with id: {}", id);
        ArticleTag entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ArticleTag", id));
        entity.suspend();
        ArticleTag updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ArticleTagReadDto activateArticleTag(Long id) {
        log.info("Activating ArticleTag with id: {}", id);
        ArticleTag entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ArticleTag", id));
        entity.activate();
        ArticleTag updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
