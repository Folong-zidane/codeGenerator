package com.example.blog.service;

import com.example.blog.entity.Paywall;
import com.example.blog.repository.PaywallRepository;
import com.example.blog.dto.PaywallCreateDto;
import com.example.blog.dto.PaywallReadDto;
import com.example.blog.dto.PaywallUpdateDto;
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

import com.example.blog.enums.PaywallStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaywallService {

    private final PaywallRepository repository;

    @Transactional(readOnly = true)
    public List<PaywallReadDto> findAll() {
        log.debug("Finding all Paywalls");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PaywallReadDto> findById(Long id) {
        log.debug("Finding Paywall by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PaywallReadDto getById(Long id) {
        log.debug("Getting Paywall by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Paywall", id));
    }

    public PaywallReadDto create(PaywallCreateDto createDto) {
        log.info("Creating new Paywall: {}", createDto);
        Paywall entity = convertToEntity(createDto);
        Paywall saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PaywallReadDto update(Long id, PaywallUpdateDto updateDto) {
        log.info("Updating Paywall with id: {}", id);
        Paywall existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paywall", id));
        
        updateEntityFromDto(existing, updateDto);
        Paywall updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Paywall with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Paywall", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PaywallReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Paywall with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PaywallReadDto convertToReadDto(Paywall entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PaywallReadDto();
    }

    private Paywall convertToEntity(PaywallCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Paywall();
    }

    private void updateEntityFromDto(Paywall entity, PaywallUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PaywallReadDto suspendPaywall(Long id) {
        log.info("Suspending Paywall with id: {}", id);
        Paywall entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paywall", id));
        entity.suspend();
        Paywall updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PaywallReadDto activatePaywall(Long id) {
        log.info("Activating Paywall with id: {}", id);
        Paywall entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paywall", id));
        entity.activate();
        Paywall updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
