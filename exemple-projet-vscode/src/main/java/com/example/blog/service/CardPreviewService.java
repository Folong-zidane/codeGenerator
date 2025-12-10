package com.example.blog.service;

import com.example.blog.entity.CardPreview;
import com.example.blog.repository.CardPreviewRepository;
import com.example.blog.dto.CardPreviewCreateDto;
import com.example.blog.dto.CardPreviewReadDto;
import com.example.blog.dto.CardPreviewUpdateDto;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.blog.enums.CardPreviewStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class CardPreviewService {

    private final CardPreviewRepository repository;

    @Transactional(readOnly = true)
    public List<CardPreviewReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CardPreviewReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CardPreviewReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("CardPreview", id));
    }

    public CardPreviewReadDto create(CardPreviewCreateDto createDto) {        CardPreview entity = convertToEntity(createDto);
        CardPreview saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CardPreviewReadDto update(Long id, CardPreviewUpdateDto updateDto) {        CardPreview existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CardPreview", id));
        
        updateEntityFromDto(existing, updateDto);
        CardPreview updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CardPreview", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CardPreviewReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CardPreviewReadDto convertToReadDto(CardPreview entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CardPreviewReadDto();
    }

    private CardPreview convertToEntity(CardPreviewCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new CardPreview();
    }

    private void updateEntityFromDto(CardPreview entity, CardPreviewUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CardPreviewReadDto suspendCardPreview(Long id) {        CardPreview entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CardPreview", id));
        entity.suspend();
        CardPreview updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CardPreviewReadDto activateCardPreview(Long id) {        CardPreview entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CardPreview", id));
        entity.activate();
        CardPreview updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
