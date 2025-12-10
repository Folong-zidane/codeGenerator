package com.example.blog.service;

import com.example.blog.entity.VideoStream;
import com.example.blog.repository.VideoStreamRepository;
import com.example.blog.dto.VideoStreamCreateDto;
import com.example.blog.dto.VideoStreamReadDto;
import com.example.blog.dto.VideoStreamUpdateDto;
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

import com.example.blog.enums.VideoStreamStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VideoStreamService {

    private final VideoStreamRepository repository;

    @Transactional(readOnly = true)
    public List<VideoStreamReadDto> findAll() {
        log.debug("Finding all VideoStreams");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<VideoStreamReadDto> findById(Long id) {
        log.debug("Finding VideoStream by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public VideoStreamReadDto getById(Long id) {
        log.debug("Getting VideoStream by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("VideoStream", id));
    }

    public VideoStreamReadDto create(VideoStreamCreateDto createDto) {
        log.info("Creating new VideoStream: {}", createDto);
        VideoStream entity = convertToEntity(createDto);
        VideoStream saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public VideoStreamReadDto update(Long id, VideoStreamUpdateDto updateDto) {
        log.info("Updating VideoStream with id: {}", id);
        VideoStream existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VideoStream", id));
        
        updateEntityFromDto(existing, updateDto);
        VideoStream updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting VideoStream with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("VideoStream", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<VideoStreamReadDto> findAll(Pageable pageable) {
        log.debug("Finding all VideoStream with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private VideoStreamReadDto convertToReadDto(VideoStream entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new VideoStreamReadDto();
    }

    private VideoStream convertToEntity(VideoStreamCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new VideoStream();
    }

    private void updateEntityFromDto(VideoStream entity, VideoStreamUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public VideoStreamReadDto suspendVideoStream(Long id) {
        log.info("Suspending VideoStream with id: {}", id);
        VideoStream entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VideoStream", id));
        entity.suspend();
        VideoStream updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public VideoStreamReadDto activateVideoStream(Long id) {
        log.info("Activating VideoStream with id: {}", id);
        VideoStream entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VideoStream", id));
        entity.activate();
        VideoStream updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
