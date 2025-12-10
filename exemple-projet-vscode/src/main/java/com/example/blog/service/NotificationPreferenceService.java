package com.example.blog.service;

import com.example.blog.entity.NotificationPreference;
import com.example.blog.repository.NotificationPreferenceRepository;
import com.example.blog.dto.NotificationPreferenceCreateDto;
import com.example.blog.dto.NotificationPreferenceReadDto;
import com.example.blog.dto.NotificationPreferenceUpdateDto;
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

import com.example.blog.enums.NotificationPreferenceStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository repository;

    @Transactional(readOnly = true)
    public List<NotificationPreferenceReadDto> findAll() {
        log.debug("Finding all NotificationPreferences");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NotificationPreferenceReadDto> findById(Long id) {
        log.debug("Finding NotificationPreference by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public NotificationPreferenceReadDto getById(Long id) {
        log.debug("Getting NotificationPreference by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("NotificationPreference", id));
    }

    public NotificationPreferenceReadDto create(NotificationPreferenceCreateDto createDto) {
        log.info("Creating new NotificationPreference: {}", createDto);
        NotificationPreference entity = convertToEntity(createDto);
        NotificationPreference saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public NotificationPreferenceReadDto update(Long id, NotificationPreferenceUpdateDto updateDto) {
        log.info("Updating NotificationPreference with id: {}", id);
        NotificationPreference existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationPreference", id));
        
        updateEntityFromDto(existing, updateDto);
        NotificationPreference updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting NotificationPreference with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("NotificationPreference", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<NotificationPreferenceReadDto> findAll(Pageable pageable) {
        log.debug("Finding all NotificationPreference with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private NotificationPreferenceReadDto convertToReadDto(NotificationPreference entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new NotificationPreferenceReadDto();
    }

    private NotificationPreference convertToEntity(NotificationPreferenceCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new NotificationPreference();
    }

    private void updateEntityFromDto(NotificationPreference entity, NotificationPreferenceUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public NotificationPreferenceReadDto suspendNotificationPreference(Long id) {
        log.info("Suspending NotificationPreference with id: {}", id);
        NotificationPreference entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationPreference", id));
        entity.suspend();
        NotificationPreference updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public NotificationPreferenceReadDto activateNotificationPreference(Long id) {
        log.info("Activating NotificationPreference with id: {}", id);
        NotificationPreference entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NotificationPreference", id));
        entity.activate();
        NotificationPreference updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
