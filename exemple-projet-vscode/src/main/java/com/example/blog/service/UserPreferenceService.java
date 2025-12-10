package com.example.blog.service;

import com.example.blog.entity.UserPreference;
import com.example.blog.repository.UserPreferenceRepository;
import com.example.blog.dto.UserPreferenceCreateDto;
import com.example.blog.dto.UserPreferenceReadDto;
import com.example.blog.dto.UserPreferenceUpdateDto;
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

import com.example.blog.enums.UserPreferenceStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPreferenceService {

    private final UserPreferenceRepository repository;

    @Transactional(readOnly = true)
    public List<UserPreferenceReadDto> findAll() {
        log.debug("Finding all UserPreferences");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserPreferenceReadDto> findById(Long id) {
        log.debug("Finding UserPreference by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public UserPreferenceReadDto getById(Long id) {
        log.debug("Getting UserPreference by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("UserPreference", id));
    }

    public UserPreferenceReadDto create(UserPreferenceCreateDto createDto) {
        log.info("Creating new UserPreference: {}", createDto);
        UserPreference entity = convertToEntity(createDto);
        UserPreference saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public UserPreferenceReadDto update(Long id, UserPreferenceUpdateDto updateDto) {
        log.info("Updating UserPreference with id: {}", id);
        UserPreference existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserPreference", id));
        
        updateEntityFromDto(existing, updateDto);
        UserPreference updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting UserPreference with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("UserPreference", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UserPreferenceReadDto> findAll(Pageable pageable) {
        log.debug("Finding all UserPreference with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private UserPreferenceReadDto convertToReadDto(UserPreference entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new UserPreferenceReadDto();
    }

    private UserPreference convertToEntity(UserPreferenceCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new UserPreference();
    }

    private void updateEntityFromDto(UserPreference entity, UserPreferenceUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public UserPreferenceReadDto suspendUserPreference(Long id) {
        log.info("Suspending UserPreference with id: {}", id);
        UserPreference entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserPreference", id));
        entity.suspend();
        UserPreference updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public UserPreferenceReadDto activateUserPreference(Long id) {
        log.info("Activating UserPreference with id: {}", id);
        UserPreference entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserPreference", id));
        entity.activate();
        UserPreference updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
