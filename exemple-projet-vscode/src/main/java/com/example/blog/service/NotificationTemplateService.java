package com.example.blog.service;

import com.example.blog.entity.NotificationTemplate;
import com.example.blog.repository.NotificationTemplateRepository;
import com.example.blog.dto.NotificationTemplateCreateDto;
import com.example.blog.dto.NotificationTemplateReadDto;
import com.example.blog.dto.NotificationTemplateUpdateDto;
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

import com.example.blog.enums.NotificationTemplateStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationTemplateService {

    private final NotificationTemplateRepository repository;

    @Transactional(readOnly = true)
    public List<NotificationTemplateReadDto> findAll() {
        log.debug("Finding all NotificationTemplates");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NotificationTemplateReadDto> findById(Long id) {
        log.debug("Finding NotificationTemplate by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public NotificationTemplateReadDto getById(Long id) {
        log.debug("Getting NotificationTemplate by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("NotificationTemplate", id));
    }

    public NotificationTemplateReadDto create(NotificationTemplateCreateDto createDto) {
        log.info("Creating new NotificationTemplate: {}", createDto);
        NotificationTemplate entity = convertToEntity(createDto);
        NotificationTemplate saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public NotificationTemplateReadDto update(Long id, NotificationTemplateUpdateDto updateDto) {
        log.info("Updating NotificationTemplate with id: {}", id);
        NotificationTemplate existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationTemplate", id));
        
        updateEntityFromDto(existing, updateDto);
        NotificationTemplate updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting NotificationTemplate with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("NotificationTemplate", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<NotificationTemplateReadDto> findAll(Pageable pageable) {
        log.debug("Finding all NotificationTemplate with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private NotificationTemplateReadDto convertToReadDto(NotificationTemplate entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new NotificationTemplateReadDto();
    }

    private NotificationTemplate convertToEntity(NotificationTemplateCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new NotificationTemplate();
    }

    private void updateEntityFromDto(NotificationTemplate entity, NotificationTemplateUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public NotificationTemplateReadDto suspendNotificationTemplate(Long id) {
        log.info("Suspending NotificationTemplate with id: {}", id);
        NotificationTemplate entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationTemplate", id));
        entity.suspend();
        NotificationTemplate updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public NotificationTemplateReadDto activateNotificationTemplate(Long id) {
        log.info("Activating NotificationTemplate with id: {}", id);
        NotificationTemplate entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationTemplate", id));
        entity.activate();
        NotificationTemplate updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
