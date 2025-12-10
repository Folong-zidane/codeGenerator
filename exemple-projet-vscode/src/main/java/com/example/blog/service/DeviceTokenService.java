package com.example.blog.service;

import com.example.blog.entity.DeviceToken;
import com.example.blog.repository.DeviceTokenRepository;
import com.example.blog.dto.DeviceTokenCreateDto;
import com.example.blog.dto.DeviceTokenReadDto;
import com.example.blog.dto.DeviceTokenUpdateDto;
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

import com.example.blog.enums.DeviceTokenStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeviceTokenService {

    private final DeviceTokenRepository repository;

    @Transactional(readOnly = true)
    public List<DeviceTokenReadDto> findAll() {
        log.debug("Finding all DeviceTokens");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DeviceTokenReadDto> findById(Long id) {
        log.debug("Finding DeviceToken by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public DeviceTokenReadDto getById(Long id) {
        log.debug("Getting DeviceToken by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("DeviceToken", id));
    }

    public DeviceTokenReadDto create(DeviceTokenCreateDto createDto) {
        log.info("Creating new DeviceToken: {}", createDto);
        DeviceToken entity = convertToEntity(createDto);
        DeviceToken saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public DeviceTokenReadDto update(Long id, DeviceTokenUpdateDto updateDto) {
        log.info("Updating DeviceToken with id: {}", id);
        DeviceToken existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DeviceToken", id));
        
        updateEntityFromDto(existing, updateDto);
        DeviceToken updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting DeviceToken with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("DeviceToken", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<DeviceTokenReadDto> findAll(Pageable pageable) {
        log.debug("Finding all DeviceToken with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private DeviceTokenReadDto convertToReadDto(DeviceToken entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new DeviceTokenReadDto();
    }

    private DeviceToken convertToEntity(DeviceTokenCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new DeviceToken();
    }

    private void updateEntityFromDto(DeviceToken entity, DeviceTokenUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public DeviceTokenReadDto suspendDeviceToken(Long id) {
        log.info("Suspending DeviceToken with id: {}", id);
        DeviceToken entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DeviceToken", id));
        entity.suspend();
        DeviceToken updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public DeviceTokenReadDto activateDeviceToken(Long id) {
        log.info("Activating DeviceToken with id: {}", id);
        DeviceToken entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DeviceToken", id));
        entity.activate();
        DeviceToken updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
