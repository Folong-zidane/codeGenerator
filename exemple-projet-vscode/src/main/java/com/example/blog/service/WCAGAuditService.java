package com.example.blog.service;

import com.example.blog.entity.WCAGAudit;
import com.example.blog.repository.WCAGAuditRepository;
import com.example.blog.dto.WCAGAuditCreateDto;
import com.example.blog.dto.WCAGAuditReadDto;
import com.example.blog.dto.WCAGAuditUpdateDto;
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

import com.example.blog.enums.WCAGAuditStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class WCAGAuditService {

    private final WCAGAuditRepository repository;

    @Transactional(readOnly = true)
    public List<WCAGAuditReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<WCAGAuditReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public WCAGAuditReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("WCAGAudit", id));
    }

    public WCAGAuditReadDto create(WCAGAuditCreateDto createDto) {        WCAGAudit entity = convertToEntity(createDto);
        WCAGAudit saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public WCAGAuditReadDto update(Long id, WCAGAuditUpdateDto updateDto) {        WCAGAudit existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("WCAGAudit", id));
        
        updateEntityFromDto(existing, updateDto);
        WCAGAudit updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("WCAGAudit", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<WCAGAuditReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private WCAGAuditReadDto convertToReadDto(WCAGAudit entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new WCAGAuditReadDto();
    }

    private WCAGAudit convertToEntity(WCAGAuditCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new WCAGAudit();
    }

    private void updateEntityFromDto(WCAGAudit entity, WCAGAuditUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public WCAGAuditReadDto suspendWCAGAudit(Long id) {        WCAGAudit entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("WCAGAudit", id));
        entity.suspend();
        WCAGAudit updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public WCAGAuditReadDto activateWCAGAudit(Long id) {        WCAGAudit entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("WCAGAudit", id));
        entity.activate();
        WCAGAudit updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
