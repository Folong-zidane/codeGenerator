package com.example.blog.service;

import com.example.blog.entity.CategoryFilter;
import com.example.blog.repository.CategoryFilterRepository;
import com.example.blog.dto.CategoryFilterCreateDto;
import com.example.blog.dto.CategoryFilterReadDto;
import com.example.blog.dto.CategoryFilterUpdateDto;
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

import com.example.blog.enums.CategoryFilterStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryFilterService {

    private final CategoryFilterRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryFilterReadDto> findAll() {
        log.debug("Finding all CategoryFilters");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryFilterReadDto> findById(Long id) {
        log.debug("Finding CategoryFilter by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CategoryFilterReadDto getById(Long id) {
        log.debug("Getting CategoryFilter by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("CategoryFilter", id));
    }

    public CategoryFilterReadDto create(CategoryFilterCreateDto createDto) {
        log.info("Creating new CategoryFilter: {}", createDto);
        CategoryFilter entity = convertToEntity(createDto);
        CategoryFilter saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CategoryFilterReadDto update(Long id, CategoryFilterUpdateDto updateDto) {
        log.info("Updating CategoryFilter with id: {}", id);
        CategoryFilter existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CategoryFilter", id));
        
        updateEntityFromDto(existing, updateDto);
        CategoryFilter updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting CategoryFilter with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CategoryFilter", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CategoryFilterReadDto> findAll(Pageable pageable) {
        log.debug("Finding all CategoryFilter with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CategoryFilterReadDto convertToReadDto(CategoryFilter entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CategoryFilterReadDto();
    }

    private CategoryFilter convertToEntity(CategoryFilterCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new CategoryFilter();
    }

    private void updateEntityFromDto(CategoryFilter entity, CategoryFilterUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CategoryFilterReadDto suspendCategoryFilter(Long id) {
        log.info("Suspending CategoryFilter with id: {}", id);
        CategoryFilter entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CategoryFilter", id));
        entity.suspend();
        CategoryFilter updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CategoryFilterReadDto activateCategoryFilter(Long id) {
        log.info("Activating CategoryFilter with id: {}", id);
        CategoryFilter entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CategoryFilter", id));
        entity.activate();
        CategoryFilter updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
