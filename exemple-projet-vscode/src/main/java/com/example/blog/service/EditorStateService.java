package com.example.blog.service;

import com.example.blog.entity.EditorState;
import com.example.blog.repository.EditorStateRepository;
import com.example.blog.dto.EditorStateCreateDto;
import com.example.blog.dto.EditorStateReadDto;
import com.example.blog.dto.EditorStateUpdateDto;
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

import com.example.blog.enums.EditorStateStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EditorStateService {

    private final EditorStateRepository repository;

    @Transactional(readOnly = true)
    public List<EditorStateReadDto> findAll() {
        log.debug("Finding all EditorStates");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EditorStateReadDto> findById(Long id) {
        log.debug("Finding EditorState by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public EditorStateReadDto getById(Long id) {
        log.debug("Getting EditorState by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("EditorState", id));
    }

    public EditorStateReadDto create(EditorStateCreateDto createDto) {
        log.info("Creating new EditorState: {}", createDto);
        EditorState entity = convertToEntity(createDto);
        EditorState saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public EditorStateReadDto update(Long id, EditorStateUpdateDto updateDto) {
        log.info("Updating EditorState with id: {}", id);
        EditorState existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EditorState", id));
        
        updateEntityFromDto(existing, updateDto);
        EditorState updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting EditorState with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("EditorState", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<EditorStateReadDto> findAll(Pageable pageable) {
        log.debug("Finding all EditorState with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private EditorStateReadDto convertToReadDto(EditorState entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new EditorStateReadDto();
    }

    private EditorState convertToEntity(EditorStateCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new EditorState();
    }

    private void updateEntityFromDto(EditorState entity, EditorStateUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public EditorStateReadDto suspendEditorState(Long id) {
        log.info("Suspending EditorState with id: {}", id);
        EditorState entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EditorState", id));
        entity.suspend();
        EditorState updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public EditorStateReadDto activateEditorState(Long id) {
        log.info("Activating EditorState with id: {}", id);
        EditorState entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EditorState", id));
        entity.activate();
        EditorState updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
