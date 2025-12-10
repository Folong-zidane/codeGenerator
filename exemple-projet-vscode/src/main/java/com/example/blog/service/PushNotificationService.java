package com.example.blog.service;

import com.example.blog.entity.PushNotification;
import com.example.blog.repository.PushNotificationRepository;
import com.example.blog.dto.PushNotificationCreateDto;
import com.example.blog.dto.PushNotificationReadDto;
import com.example.blog.dto.PushNotificationUpdateDto;
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

import com.example.blog.enums.PushNotificationStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PushNotificationService {

    private final PushNotificationRepository repository;

    @Transactional(readOnly = true)
    public List<PushNotificationReadDto> findAll() {
        log.debug("Finding all PushNotifications");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PushNotificationReadDto> findById(Long id) {
        log.debug("Finding PushNotification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PushNotificationReadDto getById(Long id) {
        log.debug("Getting PushNotification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("PushNotification", id));
    }

    public PushNotificationReadDto create(PushNotificationCreateDto createDto) {
        log.info("Creating new PushNotification: {}", createDto);
        PushNotification entity = convertToEntity(createDto);
        PushNotification saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PushNotificationReadDto update(Long id, PushNotificationUpdateDto updateDto) {
        log.info("Updating PushNotification with id: {}", id);
        PushNotification existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PushNotification", id));
        
        updateEntityFromDto(existing, updateDto);
        PushNotification updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting PushNotification with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PushNotification", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PushNotificationReadDto> findAll(Pageable pageable) {
        log.debug("Finding all PushNotification with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PushNotificationReadDto convertToReadDto(PushNotification entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PushNotificationReadDto();
    }

    private PushNotification convertToEntity(PushNotificationCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new PushNotification();
    }

    private void updateEntityFromDto(PushNotification entity, PushNotificationUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PushNotificationReadDto suspendPushNotification(Long id) {
        log.info("Suspending PushNotification with id: {}", id);
        PushNotification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PushNotification", id));
        entity.suspend();
        PushNotification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PushNotificationReadDto activatePushNotification(Long id) {
        log.info("Activating PushNotification with id: {}", id);
        PushNotification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PushNotification", id));
        entity.activate();
        PushNotification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
