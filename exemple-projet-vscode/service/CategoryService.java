package com.example.blog.service;

import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.dto.CategoryCreateDto;
import com.example.blog.dto.CategoryReadDto;
import com.example.blog.dto.CategoryUpdateDto;
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

import com.example.blog.enums.CategoryStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryReadDto> findAll() {
        log.debug("Finding all Categorys");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryReadDto> findById(Long id) {
        log.debug("Finding Category by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CategoryReadDto getById(Long id) {
        log.debug("Getting Category by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Category", id));
    }

    public CategoryReadDto create(CategoryCreateDto createDto) {
        log.info("Creating new Category: {}", createDto);
        Category entity = convertToEntity(createDto);
        Category saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CategoryReadDto update(Long id, CategoryUpdateDto updateDto) {
        log.info("Updating Category with id: {}", id);
        Category existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category", id));
        
        updateEntityFromDto(existing, updateDto);
        Category updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Category with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Category", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CategoryReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Category with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CategoryReadDto convertToReadDto(Category entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CategoryReadDto();
    }

    private Category convertToEntity(CategoryCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Category();
    }

    private void updateEntityFromDto(Category entity, CategoryUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CategoryReadDto suspendCategory(Long id) {
        log.info("Suspending Category with id: {}", id);
        Category entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category", id));
        entity.suspend();
        Category updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CategoryReadDto activateCategory(Long id) {
        log.info("Activating Category with id: {}", id);
        Category entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category", id));
        entity.activate();
        Category updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
