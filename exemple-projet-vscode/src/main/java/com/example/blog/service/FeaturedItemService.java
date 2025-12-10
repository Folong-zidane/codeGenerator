package com.example.blog.service;

import com.example.blog.entity.FeaturedItem;
import com.example.blog.repository.FeaturedItemRepository;
import com.example.blog.dto.FeaturedItemCreateDto;
import com.example.blog.dto.FeaturedItemReadDto;
import com.example.blog.dto.FeaturedItemUpdateDto;
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

import com.example.blog.enums.FeaturedItemStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeaturedItemService {

    private final FeaturedItemRepository repository;

    @Transactional(readOnly = true)
    public List<FeaturedItemReadDto> findAll() {
        log.debug("Finding all FeaturedItems");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<FeaturedItemReadDto> findById(Long id) {
        log.debug("Finding FeaturedItem by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public FeaturedItemReadDto getById(Long id) {
        log.debug("Getting FeaturedItem by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("FeaturedItem", id));
    }

    public FeaturedItemReadDto create(FeaturedItemCreateDto createDto) {
        log.info("Creating new FeaturedItem: {}", createDto);
        FeaturedItem entity = convertToEntity(createDto);
        FeaturedItem saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public FeaturedItemReadDto update(Long id, FeaturedItemUpdateDto updateDto) {
        log.info("Updating FeaturedItem with id: {}", id);
        FeaturedItem existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("FeaturedItem", id));
        
        updateEntityFromDto(existing, updateDto);
        FeaturedItem updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting FeaturedItem with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("FeaturedItem", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<FeaturedItemReadDto> findAll(Pageable pageable) {
        log.debug("Finding all FeaturedItem with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private FeaturedItemReadDto convertToReadDto(FeaturedItem entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new FeaturedItemReadDto();
    }

    private FeaturedItem convertToEntity(FeaturedItemCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new FeaturedItem();
    }

    private void updateEntityFromDto(FeaturedItem entity, FeaturedItemUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public FeaturedItemReadDto suspendFeaturedItem(Long id) {
        log.info("Suspending FeaturedItem with id: {}", id);
        FeaturedItem entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("FeaturedItem", id));
        entity.suspend();
        FeaturedItem updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public FeaturedItemReadDto activateFeaturedItem(Long id) {
        log.info("Activating FeaturedItem with id: {}", id);
        FeaturedItem entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("FeaturedItem", id));
        entity.activate();
        FeaturedItem updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
