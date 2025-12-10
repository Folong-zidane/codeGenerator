package com.example.blog.service;

import com.example.blog.entity.AudioTrack;
import com.example.blog.repository.AudioTrackRepository;
import com.example.blog.dto.AudioTrackCreateDto;
import com.example.blog.dto.AudioTrackReadDto;
import com.example.blog.dto.AudioTrackUpdateDto;
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

import com.example.blog.enums.AudioTrackStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AudioTrackService {

    private final AudioTrackRepository repository;

    @Transactional(readOnly = true)
    public List<AudioTrackReadDto> findAll() {
        log.debug("Finding all AudioTracks");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AudioTrackReadDto> findById(Long id) {
        log.debug("Finding AudioTrack by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public AudioTrackReadDto getById(Long id) {
        log.debug("Getting AudioTrack by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("AudioTrack", id));
    }

    public AudioTrackReadDto create(AudioTrackCreateDto createDto) {
        log.info("Creating new AudioTrack: {}", createDto);
        AudioTrack entity = convertToEntity(createDto);
        AudioTrack saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public AudioTrackReadDto update(Long id, AudioTrackUpdateDto updateDto) {
        log.info("Updating AudioTrack with id: {}", id);
        AudioTrack existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AudioTrack", id));
        
        updateEntityFromDto(existing, updateDto);
        AudioTrack updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting AudioTrack with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("AudioTrack", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AudioTrackReadDto> findAll(Pageable pageable) {
        log.debug("Finding all AudioTrack with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private AudioTrackReadDto convertToReadDto(AudioTrack entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new AudioTrackReadDto();
    }

    private AudioTrack convertToEntity(AudioTrackCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new AudioTrack();
    }

    private void updateEntityFromDto(AudioTrack entity, AudioTrackUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public AudioTrackReadDto suspendAudioTrack(Long id) {
        log.info("Suspending AudioTrack with id: {}", id);
        AudioTrack entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AudioTrack", id));
        entity.suspend();
        AudioTrack updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public AudioTrackReadDto activateAudioTrack(Long id) {
        log.info("Activating AudioTrack with id: {}", id);
        AudioTrack entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AudioTrack", id));
        entity.activate();
        AudioTrack updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
