package com.example.blog.service;

import com.example.blog.entity.Notification;
import com.example.blog.repository.NotificationRepository;
import com.example.blog.dto.NotificationCreateDto;
import com.example.blog.dto.NotificationReadDto;
import com.example.blog.dto.NotificationUpdateDto;
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

import com.example.blog.enums.NotificationStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional(readOnly = true)
    public List<NotificationReadDto> findAll() {
        log.debug("Finding all Notifications");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NotificationReadDto> findById(Long id) {
        log.debug("Finding Notification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public NotificationReadDto getById(Long id) {
        log.debug("Getting Notification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Notification", id));
    }

    public NotificationReadDto create(NotificationCreateDto createDto) {
        log.info("Creating new Notification: {}", createDto);
        Notification entity = convertToEntity(createDto);
        Notification saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public NotificationReadDto update(Long id, NotificationUpdateDto updateDto) {
        log.info("Updating Notification with id: {}", id);
        Notification existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        
        updateEntityFromDto(existing, updateDto);
        Notification updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Notification with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Notification", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<NotificationReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Notification with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private NotificationReadDto convertToReadDto(Notification entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new NotificationReadDto();
    }

    private Notification convertToEntity(NotificationCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Notification();
    }

    private void updateEntityFromDto(Notification entity, NotificationUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public NotificationReadDto suspendNotification(Long id) {
        log.info("Suspending Notification with id: {}", id);
        Notification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        entity.suspend();
        Notification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public NotificationReadDto activateNotification(Long id) {
        log.info("Activating Notification with id: {}", id);
        Notification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        entity.activate();
        Notification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
