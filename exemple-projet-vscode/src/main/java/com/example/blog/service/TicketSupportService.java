package com.example.blog.service;

import com.example.blog.entity.TicketSupport;
import com.example.blog.repository.TicketSupportRepository;
import com.example.blog.dto.TicketSupportCreateDto;
import com.example.blog.dto.TicketSupportReadDto;
import com.example.blog.dto.TicketSupportUpdateDto;
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

import com.example.blog.enums.TicketSupportStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketSupportService {

    private final TicketSupportRepository repository;

    @Transactional(readOnly = true)
    public List<TicketSupportReadDto> findAll() {
        log.debug("Finding all TicketSupports");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TicketSupportReadDto> findById(Long id) {
        log.debug("Finding TicketSupport by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public TicketSupportReadDto getById(Long id) {
        log.debug("Getting TicketSupport by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("TicketSupport", id));
    }

    public TicketSupportReadDto create(TicketSupportCreateDto createDto) {
        log.info("Creating new TicketSupport: {}", createDto);
        TicketSupport entity = convertToEntity(createDto);
        TicketSupport saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public TicketSupportReadDto update(Long id, TicketSupportUpdateDto updateDto) {
        log.info("Updating TicketSupport with id: {}", id);
        TicketSupport existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TicketSupport", id));
        
        updateEntityFromDto(existing, updateDto);
        TicketSupport updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting TicketSupport with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("TicketSupport", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TicketSupportReadDto> findAll(Pageable pageable) {
        log.debug("Finding all TicketSupport with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private TicketSupportReadDto convertToReadDto(TicketSupport entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new TicketSupportReadDto();
    }

    private TicketSupport convertToEntity(TicketSupportCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new TicketSupport();
    }

    private void updateEntityFromDto(TicketSupport entity, TicketSupportUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public TicketSupportReadDto suspendTicketSupport(Long id) {
        log.info("Suspending TicketSupport with id: {}", id);
        TicketSupport entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TicketSupport", id));
        entity.suspend();
        TicketSupport updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public TicketSupportReadDto activateTicketSupport(Long id) {
        log.info("Activating TicketSupport with id: {}", id);
        TicketSupport entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TicketSupport", id));
        entity.activate();
        TicketSupport updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
