package com.example.blog.service;

import com.example.blog.entity.Comment;
import com.example.blog.repository.CommentRepository;
import com.example.blog.dto.CommentCreateDto;
import com.example.blog.dto.CommentReadDto;
import com.example.blog.dto.CommentUpdateDto;
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

import com.example.blog.enums.CommentStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository repository;

    @Transactional(readOnly = true)
    public List<CommentReadDto> findAll() {
        log.debug("Finding all Comments");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CommentReadDto> findById(Long id) {
        log.debug("Finding Comment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CommentReadDto getById(Long id) {
        log.debug("Getting Comment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Comment", id));
    }

    public CommentReadDto create(CommentCreateDto createDto) {
        log.info("Creating new Comment: {}", createDto);
        Comment entity = convertToEntity(createDto);
        Comment saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CommentReadDto update(Long id, CommentUpdateDto updateDto) {
        log.info("Updating Comment with id: {}", id);
        Comment existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment", id));
        
        updateEntityFromDto(existing, updateDto);
        Comment updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Comment with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Comment", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CommentReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Comment with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CommentReadDto convertToReadDto(Comment entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CommentReadDto();
    }

    private Comment convertToEntity(CommentCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Comment();
    }

    private void updateEntityFromDto(Comment entity, CommentUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CommentReadDto suspendComment(Long id) {
        log.info("Suspending Comment with id: {}", id);
        Comment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment", id));
        entity.suspend();
        Comment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CommentReadDto activateComment(Long id) {
        log.info("Activating Comment with id: {}", id);
        Comment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment", id));
        entity.activate();
        Comment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
