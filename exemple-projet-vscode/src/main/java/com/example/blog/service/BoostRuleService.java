package com.example.blog.service;

import com.example.blog.entity.BoostRule;
import com.example.blog.repository.BoostRuleRepository;
import com.example.blog.dto.BoostRuleCreateDto;
import com.example.blog.dto.BoostRuleReadDto;
import com.example.blog.dto.BoostRuleUpdateDto;
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

import com.example.blog.enums.BoostRuleStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoostRuleService {

    private final BoostRuleRepository repository;

    @Transactional(readOnly = true)
    public List<BoostRuleReadDto> findAll() {
        log.debug("Finding all BoostRules");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<BoostRuleReadDto> findById(Long id) {
        log.debug("Finding BoostRule by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public BoostRuleReadDto getById(Long id) {
        log.debug("Getting BoostRule by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("BoostRule", id));
    }

    public BoostRuleReadDto create(BoostRuleCreateDto createDto) {
        log.info("Creating new BoostRule: {}", createDto);
        BoostRule entity = convertToEntity(createDto);
        BoostRule saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public BoostRuleReadDto update(Long id, BoostRuleUpdateDto updateDto) {
        log.info("Updating BoostRule with id: {}", id);
        BoostRule existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BoostRule", id));
        
        updateEntityFromDto(existing, updateDto);
        BoostRule updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting BoostRule with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("BoostRule", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<BoostRuleReadDto> findAll(Pageable pageable) {
        log.debug("Finding all BoostRule with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private BoostRuleReadDto convertToReadDto(BoostRule entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new BoostRuleReadDto();
    }

    private BoostRule convertToEntity(BoostRuleCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new BoostRule();
    }

    private void updateEntityFromDto(BoostRule entity, BoostRuleUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public BoostRuleReadDto suspendBoostRule(Long id) {
        log.info("Suspending BoostRule with id: {}", id);
        BoostRule entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BoostRule", id));
        entity.suspend();
        BoostRule updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public BoostRuleReadDto activateBoostRule(Long id) {
        log.info("Activating BoostRule with id: {}", id);
        BoostRule entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BoostRule", id));
        entity.activate();
        BoostRule updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
