package com.example.blog.service;

import com.example.blog.entity.Sitemap;
import com.example.blog.repository.SitemapRepository;
import com.example.blog.dto.SitemapCreateDto;
import com.example.blog.dto.SitemapReadDto;
import com.example.blog.dto.SitemapUpdateDto;
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

import com.example.blog.enums.SitemapStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SitemapService {

    private final SitemapRepository repository;

    @Transactional(readOnly = true)
    public List<SitemapReadDto> findAll() {
        log.debug("Finding all Sitemaps");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SitemapReadDto> findById(Long id) {
        log.debug("Finding Sitemap by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public SitemapReadDto getById(Long id) {
        log.debug("Getting Sitemap by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Sitemap", id));
    }

    public SitemapReadDto create(SitemapCreateDto createDto) {
        log.info("Creating new Sitemap: {}", createDto);
        Sitemap entity = convertToEntity(createDto);
        Sitemap saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public SitemapReadDto update(Long id, SitemapUpdateDto updateDto) {
        log.info("Updating Sitemap with id: {}", id);
        Sitemap existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sitemap", id));
        
        updateEntityFromDto(existing, updateDto);
        Sitemap updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Sitemap with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Sitemap", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SitemapReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Sitemap with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private SitemapReadDto convertToReadDto(Sitemap entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new SitemapReadDto();
    }

    private Sitemap convertToEntity(SitemapCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Sitemap();
    }

    private void updateEntityFromDto(Sitemap entity, SitemapUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public SitemapReadDto suspendSitemap(Long id) {
        log.info("Suspending Sitemap with id: {}", id);
        Sitemap entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sitemap", id));
        entity.suspend();
        Sitemap updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public SitemapReadDto activateSitemap(Long id) {
        log.info("Activating Sitemap with id: {}", id);
        Sitemap entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sitemap", id));
        entity.activate();
        Sitemap updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
