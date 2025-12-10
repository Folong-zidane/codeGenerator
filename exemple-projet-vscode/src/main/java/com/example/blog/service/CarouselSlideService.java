package com.example.blog.service;

import com.example.blog.entity.CarouselSlide;
import com.example.blog.repository.CarouselSlideRepository;
import com.example.blog.dto.CarouselSlideCreateDto;
import com.example.blog.dto.CarouselSlideReadDto;
import com.example.blog.dto.CarouselSlideUpdateDto;
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

import com.example.blog.enums.CarouselSlideStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CarouselSlideService {

    private final CarouselSlideRepository repository;

    @Transactional(readOnly = true)
    public List<CarouselSlideReadDto> findAll() {
        log.debug("Finding all CarouselSlides");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CarouselSlideReadDto> findById(Long id) {
        log.debug("Finding CarouselSlide by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CarouselSlideReadDto getById(Long id) {
        log.debug("Getting CarouselSlide by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("CarouselSlide", id));
    }

    public CarouselSlideReadDto create(CarouselSlideCreateDto createDto) {
        log.info("Creating new CarouselSlide: {}", createDto);
        CarouselSlide entity = convertToEntity(createDto);
        CarouselSlide saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CarouselSlideReadDto update(Long id, CarouselSlideUpdateDto updateDto) {
        log.info("Updating CarouselSlide with id: {}", id);
        CarouselSlide existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CarouselSlide", id));
        
        updateEntityFromDto(existing, updateDto);
        CarouselSlide updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting CarouselSlide with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CarouselSlide", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CarouselSlideReadDto> findAll(Pageable pageable) {
        log.debug("Finding all CarouselSlide with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CarouselSlideReadDto convertToReadDto(CarouselSlide entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CarouselSlideReadDto();
    }

    private CarouselSlide convertToEntity(CarouselSlideCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new CarouselSlide();
    }

    private void updateEntityFromDto(CarouselSlide entity, CarouselSlideUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CarouselSlideReadDto suspendCarouselSlide(Long id) {
        log.info("Suspending CarouselSlide with id: {}", id);
        CarouselSlide entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CarouselSlide", id));
        entity.suspend();
        CarouselSlide updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CarouselSlideReadDto activateCarouselSlide(Long id) {
        log.info("Activating CarouselSlide with id: {}", id);
        CarouselSlide entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CarouselSlide", id));
        entity.activate();
        CarouselSlide updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
