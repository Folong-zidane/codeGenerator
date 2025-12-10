package com.example.blog.service;

import com.example.blog.entity.AbonnementNewsletter;
import com.example.blog.repository.AbonnementNewsletterRepository;
import com.example.blog.dto.AbonnementNewsletterCreateDto;
import com.example.blog.dto.AbonnementNewsletterReadDto;
import com.example.blog.dto.AbonnementNewsletterUpdateDto;
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

import com.example.blog.enums.AbonnementNewsletterStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AbonnementNewsletterService {

    private final AbonnementNewsletterRepository repository;

    @Transactional(readOnly = true)
    public List<AbonnementNewsletterReadDto> findAll() {
        log.debug("Finding all AbonnementNewsletters");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AbonnementNewsletterReadDto> findById(Long id) {
        log.debug("Finding AbonnementNewsletter by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public AbonnementNewsletterReadDto getById(Long id) {
        log.debug("Getting AbonnementNewsletter by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementNewsletter", id));
    }

    public AbonnementNewsletterReadDto create(AbonnementNewsletterCreateDto createDto) {
        log.info("Creating new AbonnementNewsletter: {}", createDto);
        AbonnementNewsletter entity = convertToEntity(createDto);
        AbonnementNewsletter saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public AbonnementNewsletterReadDto update(Long id, AbonnementNewsletterUpdateDto updateDto) {
        log.info("Updating AbonnementNewsletter with id: {}", id);
        AbonnementNewsletter existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementNewsletter", id));
        
        updateEntityFromDto(existing, updateDto);
        AbonnementNewsletter updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting AbonnementNewsletter with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("AbonnementNewsletter", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AbonnementNewsletterReadDto> findAll(Pageable pageable) {
        log.debug("Finding all AbonnementNewsletter with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private AbonnementNewsletterReadDto convertToReadDto(AbonnementNewsletter entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new AbonnementNewsletterReadDto();
    }

    private AbonnementNewsletter convertToEntity(AbonnementNewsletterCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new AbonnementNewsletter();
    }

    private void updateEntityFromDto(AbonnementNewsletter entity, AbonnementNewsletterUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public AbonnementNewsletterReadDto suspendAbonnementNewsletter(Long id) {
        log.info("Suspending AbonnementNewsletter with id: {}", id);
        AbonnementNewsletter entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementNewsletter", id));
        entity.suspend();
        AbonnementNewsletter updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public AbonnementNewsletterReadDto activateAbonnementNewsletter(Long id) {
        log.info("Activating AbonnementNewsletter with id: {}", id);
        AbonnementNewsletter entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AbonnementNewsletter", id));
        entity.activate();
        AbonnementNewsletter updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
