package com.example.blog.service;

import com.example.blog.entity.ContratMaintenance;
import com.example.blog.repository.ContratMaintenanceRepository;
import com.example.blog.dto.ContratMaintenanceCreateDto;
import com.example.blog.dto.ContratMaintenanceReadDto;
import com.example.blog.dto.ContratMaintenanceUpdateDto;
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

import com.example.blog.enums.ContratMaintenanceStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratMaintenanceService {

    private final ContratMaintenanceRepository repository;

    @Transactional(readOnly = true)
    public List<ContratMaintenanceReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ContratMaintenanceReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ContratMaintenanceReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("ContratMaintenance", id));
    }

    public ContratMaintenanceReadDto create(ContratMaintenanceCreateDto createDto) {        ContratMaintenance entity = convertToEntity(createDto);
        ContratMaintenance saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ContratMaintenanceReadDto update(Long id, ContratMaintenanceUpdateDto updateDto) {        ContratMaintenance existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ContratMaintenance", id));
        
        updateEntityFromDto(existing, updateDto);
        ContratMaintenance updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ContratMaintenance", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ContratMaintenanceReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ContratMaintenanceReadDto convertToReadDto(ContratMaintenance entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ContratMaintenanceReadDto();
    }

    private ContratMaintenance convertToEntity(ContratMaintenanceCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new ContratMaintenance();
    }

    private void updateEntityFromDto(ContratMaintenance entity, ContratMaintenanceUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ContratMaintenanceReadDto suspendContratMaintenance(Long id) {        ContratMaintenance entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ContratMaintenance", id));
        entity.suspend();
        ContratMaintenance updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ContratMaintenanceReadDto activateContratMaintenance(Long id) {        ContratMaintenance entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ContratMaintenance", id));
        entity.activate();
        ContratMaintenance updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
