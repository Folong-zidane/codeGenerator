package com.example.blog.service;

import com.example.blog.entity.PerformanceMetric;
import com.example.blog.repository.PerformanceMetricRepository;
import com.example.blog.dto.PerformanceMetricCreateDto;
import com.example.blog.dto.PerformanceMetricReadDto;
import com.example.blog.dto.PerformanceMetricUpdateDto;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.blog.enums.PerformanceMetricStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceMetricService {

    private final PerformanceMetricRepository repository;

    @Transactional(readOnly = true)
    public List<PerformanceMetricReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PerformanceMetricReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PerformanceMetricReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("PerformanceMetric", id));
    }

    public PerformanceMetricReadDto create(PerformanceMetricCreateDto createDto) {        PerformanceMetric entity = convertToEntity(createDto);
        PerformanceMetric saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PerformanceMetricReadDto update(Long id, PerformanceMetricUpdateDto updateDto) {        PerformanceMetric existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PerformanceMetric", id));
        
        updateEntityFromDto(existing, updateDto);
        PerformanceMetric updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PerformanceMetric", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PerformanceMetricReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PerformanceMetricReadDto convertToReadDto(PerformanceMetric entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PerformanceMetricReadDto();
    }

    private PerformanceMetric convertToEntity(PerformanceMetricCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new PerformanceMetric();
    }

    private void updateEntityFromDto(PerformanceMetric entity, PerformanceMetricUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PerformanceMetricReadDto suspendPerformanceMetric(Long id) {        PerformanceMetric entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PerformanceMetric", id));
        entity.suspend();
        PerformanceMetric updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PerformanceMetricReadDto activatePerformanceMetric(Long id) {        PerformanceMetric entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PerformanceMetric", id));
        entity.activate();
        PerformanceMetric updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
