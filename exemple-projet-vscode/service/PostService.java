package com.example.blog.service;

import com.example.blog.entity.Post;
import com.example.blog.repository.PostRepository;
import com.example.blog.dto.PostCreateDto;
import com.example.blog.dto.PostReadDto;
import com.example.blog.dto.PostUpdateDto;
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

import com.example.blog.enums.PostStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository repository;

    @Transactional(readOnly = true)
    public List<PostReadDto> findAll() {
        log.debug("Finding all Posts");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PostReadDto> findById(Long id) {
        log.debug("Finding Post by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PostReadDto getById(Long id) {
        log.debug("Getting Post by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Post", id));
    }

    public PostReadDto create(PostCreateDto createDto) {
        log.info("Creating new Post: {}", createDto);
        Post entity = convertToEntity(createDto);
        Post saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PostReadDto update(Long id, PostUpdateDto updateDto) {
        log.info("Updating Post with id: {}", id);
        Post existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Post", id));
        
        updateEntityFromDto(existing, updateDto);
        Post updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Post with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Post", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PostReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Post with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PostReadDto convertToReadDto(Post entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PostReadDto();
    }

    private Post convertToEntity(PostCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Post();
    }

    private void updateEntityFromDto(Post entity, PostUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PostReadDto suspendPost(Long id) {
        log.info("Suspending Post with id: {}", id);
        Post entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Post", id));
        entity.suspend();
        Post updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PostReadDto activatePost(Long id) {
        log.info("Activating Post with id: {}", id);
        Post entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Post", id));
        entity.activate();
        Post updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
