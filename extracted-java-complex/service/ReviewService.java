package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Review;
import com.ecommerce.complex.repository.ReviewRepository;
import com.ecommerce.complex.dto.ReviewCreateDto;
import com.ecommerce.complex.dto.ReviewReadDto;
import com.ecommerce.complex.dto.ReviewUpdateDto;
import com.ecommerce.complex.exception.EntityNotFoundException;
import com.ecommerce.complex.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.complex.enums.ReviewStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository repository;

    @Transactional(readOnly = true)
    public List<ReviewReadDto> findAll() {
        log.debug("Finding all Reviews");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ReviewReadDto> findById(Long id) {
        log.debug("Finding Review by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ReviewReadDto getById(Long id) {
        log.debug("Getting Review by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Review", id));
    }

    public ReviewReadDto create(ReviewCreateDto createDto) {
        log.info("Creating new Review: {}", createDto);
        Review entity = convertToEntity(createDto);
        Review saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ReviewReadDto update(Long id, ReviewUpdateDto updateDto) {
        log.info("Updating Review with id: {}", id);
        Review existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review", id));
        
        updateEntityFromDto(existing, updateDto);
        Review updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Review with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Review", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ReviewReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Review with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ReviewReadDto convertToReadDto(Review entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ReviewReadDto();
    }

    private Review convertToEntity(ReviewCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Review();
    }

    private void updateEntityFromDto(Review entity, ReviewUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ReviewReadDto suspendReview(Long id) {
        log.info("Suspending Review with id: {}", id);
        Review entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review", id));
        entity.suspend();
        Review updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ReviewReadDto activateReview(Long id) {
        log.info("Activating Review with id: {}", id);
        Review entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review", id));
        entity.activate();
        Review updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
