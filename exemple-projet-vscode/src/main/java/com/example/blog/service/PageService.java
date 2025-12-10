package com.example.blog.service;

import com.example.blog.entity.Page;
import com.example.blog.repository.PageRepository;
import com.example.blog.dto.PageCreateDto;
import com.example.blog.dto.PageReadDto;
import com.example.blog.dto.PageUpdateDto;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.blog.enums.PageStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PageService {

    private final PageRepository repository;

    @Transactional(readOnly = true)
    public List<PageReadDto> findAll() {
        log.debug("Finding all Pages");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PageReadDto> findById(Long id) {
        log.debug("Finding Page by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PageReadDto getById(Long id) {
        log.debug("Getting Page by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Page", id));
    }

    public PageReadDto create(PageCreateDto createDto) {
        log.info("Creating new Page: {}", createDto);
        Page entity = convertToEntity(createDto);
        Page saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PageReadDto update(Long id, PageUpdateDto updateDto) {
        log.info("Updating Page with id: {}", id);
        Page existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Page", id));
        
        updateEntityFromDto(existing, updateDto);
        Page updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Page with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Page", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<PageReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Page with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PageReadDto convertToReadDto(Page entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PageReadDto();
    }

    private Page convertToEntity(PageCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Page();
    }

    private void updateEntityFromDto(Page entity, PageUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PageReadDto suspendPage(Long id) {
        log.info("Suspending Page with id: {}", id);
        Page entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Page", id));
        entity.suspend();
        Page updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PageReadDto activatePage(Long id) {
        log.info("Activating Page with id: {}", id);
        Page entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Page", id));
        entity.activate();
        Page updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
