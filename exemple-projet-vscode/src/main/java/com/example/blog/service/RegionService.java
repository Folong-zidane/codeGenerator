package com.example.blog.service;

import com.example.blog.entity.Region;
import com.example.blog.repository.RegionRepository;
import com.example.blog.dto.RegionCreateDto;
import com.example.blog.dto.RegionReadDto;
import com.example.blog.dto.RegionUpdateDto;
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

import com.example.blog.enums.RegionStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RegionService {

    private final RegionRepository repository;

    @Transactional(readOnly = true)
    public List<RegionReadDto> findAll() {
        log.debug("Finding all Regions");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RegionReadDto> findById(Long id) {
        log.debug("Finding Region by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public RegionReadDto getById(Long id) {
        log.debug("Getting Region by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Region", id));
    }

    public RegionReadDto create(RegionCreateDto createDto) {
        log.info("Creating new Region: {}", createDto);
        Region entity = convertToEntity(createDto);
        Region saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public RegionReadDto update(Long id, RegionUpdateDto updateDto) {
        log.info("Updating Region with id: {}", id);
        Region existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Region", id));
        
        updateEntityFromDto(existing, updateDto);
        Region updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Region with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Region", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<RegionReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Region with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private RegionReadDto convertToReadDto(Region entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new RegionReadDto();
    }

    private Region convertToEntity(RegionCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Region();
    }

    private void updateEntityFromDto(Region entity, RegionUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public RegionReadDto suspendRegion(Long id) {
        log.info("Suspending Region with id: {}", id);
        Region entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Region", id));
        entity.suspend();
        Region updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public RegionReadDto activateRegion(Long id) {
        log.info("Activating Region with id: {}", id);
        Region entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Region", id));
        entity.activate();
        Region updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
