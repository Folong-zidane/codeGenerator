package com.example.blog.service;

import com.example.blog.entity.Tag;
import com.example.blog.repository.TagRepository;
import com.example.blog.dto.TagCreateDto;
import com.example.blog.dto.TagReadDto;
import com.example.blog.dto.TagUpdateDto;
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

import com.example.blog.enums.TagStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TagService {

    private final TagRepository repository;

    @Transactional(readOnly = true)
    public List<TagReadDto> findAll() {
        log.debug("Finding all Tags");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TagReadDto> findById(Long id) {
        log.debug("Finding Tag by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public TagReadDto getById(Long id) {
        log.debug("Getting Tag by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Tag", id));
    }

    public TagReadDto create(TagCreateDto createDto) {
        log.info("Creating new Tag: {}", createDto);
        Tag entity = convertToEntity(createDto);
        Tag saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public TagReadDto update(Long id, TagUpdateDto updateDto) {
        log.info("Updating Tag with id: {}", id);
        Tag existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag", id));
        
        updateEntityFromDto(existing, updateDto);
        Tag updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Tag with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tag", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TagReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Tag with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private TagReadDto convertToReadDto(Tag entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new TagReadDto();
    }

    private Tag convertToEntity(TagCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Tag();
    }

    private void updateEntityFromDto(Tag entity, TagUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public TagReadDto suspendTag(Long id) {
        log.info("Suspending Tag with id: {}", id);
        Tag entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag", id));
        entity.suspend();
        Tag updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public TagReadDto activateTag(Long id) {
        log.info("Activating Tag with id: {}", id);
        Tag entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag", id));
        entity.activate();
        Tag updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
