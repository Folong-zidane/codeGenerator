package com.example.blog.service;

import com.example.blog.entity.PartageConfig;
import com.example.blog.repository.PartageConfigRepository;
import com.example.blog.dto.PartageConfigCreateDto;
import com.example.blog.dto.PartageConfigReadDto;
import com.example.blog.dto.PartageConfigUpdateDto;
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

import com.example.blog.enums.PartageConfigStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class PartageConfigService {

    private final PartageConfigRepository repository;

    @Transactional(readOnly = true)
    public List<PartageConfigReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PartageConfigReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PartageConfigReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("PartageConfig", id));
    }

    public PartageConfigReadDto create(PartageConfigCreateDto createDto) {        PartageConfig entity = convertToEntity(createDto);
        PartageConfig saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PartageConfigReadDto update(Long id, PartageConfigUpdateDto updateDto) {        PartageConfig existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageConfig", id));
        
        updateEntityFromDto(existing, updateDto);
        PartageConfig updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PartageConfig", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PartageConfigReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PartageConfigReadDto convertToReadDto(PartageConfig entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PartageConfigReadDto();
    }

    private PartageConfig convertToEntity(PartageConfigCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new PartageConfig();
    }

    private void updateEntityFromDto(PartageConfig entity, PartageConfigUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PartageConfigReadDto suspendPartageConfig(Long id) {        PartageConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageConfig", id));
        entity.suspend();
        PartageConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PartageConfigReadDto activatePartageConfig(Long id) {        PartageConfig entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PartageConfig", id));
        entity.activate();
        PartageConfig updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
