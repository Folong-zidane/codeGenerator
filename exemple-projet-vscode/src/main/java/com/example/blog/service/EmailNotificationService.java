package com.example.blog.service;

import com.example.blog.entity.EmailNotification;
import com.example.blog.repository.EmailNotificationRepository;
import com.example.blog.dto.EmailNotificationCreateDto;
import com.example.blog.dto.EmailNotificationReadDto;
import com.example.blog.dto.EmailNotificationUpdateDto;
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

import com.example.blog.enums.EmailNotificationStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailNotificationService {

    private final EmailNotificationRepository repository;

    @Transactional(readOnly = true)
    public List<EmailNotificationReadDto> findAll() {
        log.debug("Finding all EmailNotifications");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EmailNotificationReadDto> findById(Long id) {
        log.debug("Finding EmailNotification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public EmailNotificationReadDto getById(Long id) {
        log.debug("Getting EmailNotification by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("EmailNotification", id));
    }

    public EmailNotificationReadDto create(EmailNotificationCreateDto createDto) {
        log.info("Creating new EmailNotification: {}", createDto);
        EmailNotification entity = convertToEntity(createDto);
        EmailNotification saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public EmailNotificationReadDto update(Long id, EmailNotificationUpdateDto updateDto) {
        log.info("Updating EmailNotification with id: {}", id);
        EmailNotification existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EmailNotification", id));
        
        updateEntityFromDto(existing, updateDto);
        EmailNotification updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting EmailNotification with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("EmailNotification", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<EmailNotificationReadDto> findAll(Pageable pageable) {
        log.debug("Finding all EmailNotification with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private EmailNotificationReadDto convertToReadDto(EmailNotification entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new EmailNotificationReadDto();
    }

    private EmailNotification convertToEntity(EmailNotificationCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new EmailNotification();
    }

    private void updateEntityFromDto(EmailNotification entity, EmailNotificationUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public EmailNotificationReadDto suspendEmailNotification(Long id) {
        log.info("Suspending EmailNotification with id: {}", id);
        EmailNotification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EmailNotification", id));
        entity.suspend();
        EmailNotification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public EmailNotificationReadDto activateEmailNotification(Long id) {
        log.info("Activating EmailNotification with id: {}", id);
        EmailNotification entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EmailNotification", id));
        entity.activate();
        EmailNotification updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
