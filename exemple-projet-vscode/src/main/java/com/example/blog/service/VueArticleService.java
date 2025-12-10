package com.example.blog.service;

import com.example.blog.entity.VueArticle;
import com.example.blog.repository.VueArticleRepository;
import com.example.blog.dto.VueArticleCreateDto;
import com.example.blog.dto.VueArticleReadDto;
import com.example.blog.dto.VueArticleUpdateDto;
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

import com.example.blog.enums.VueArticleStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VueArticleService {

    private final VueArticleRepository repository;

    @Transactional(readOnly = true)
    public List<VueArticleReadDto> findAll() {
        log.debug("Finding all VueArticles");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<VueArticleReadDto> findById(Long id) {
        log.debug("Finding VueArticle by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public VueArticleReadDto getById(Long id) {
        log.debug("Getting VueArticle by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("VueArticle", id));
    }

    public VueArticleReadDto create(VueArticleCreateDto createDto) {
        log.info("Creating new VueArticle: {}", createDto);
        VueArticle entity = convertToEntity(createDto);
        VueArticle saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public VueArticleReadDto update(Long id, VueArticleUpdateDto updateDto) {
        log.info("Updating VueArticle with id: {}", id);
        VueArticle existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VueArticle", id));
        
        updateEntityFromDto(existing, updateDto);
        VueArticle updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting VueArticle with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("VueArticle", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<VueArticleReadDto> findAll(Pageable pageable) {
        log.debug("Finding all VueArticle with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private VueArticleReadDto convertToReadDto(VueArticle entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new VueArticleReadDto();
    }

    private VueArticle convertToEntity(VueArticleCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new VueArticle();
    }

    private void updateEntityFromDto(VueArticle entity, VueArticleUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public VueArticleReadDto suspendVueArticle(Long id) {
        log.info("Suspending VueArticle with id: {}", id);
        VueArticle entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VueArticle", id));
        entity.suspend();
        VueArticle updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public VueArticleReadDto activateVueArticle(Long id) {
        log.info("Activating VueArticle with id: {}", id);
        VueArticle entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VueArticle", id));
        entity.activate();
        VueArticle updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
