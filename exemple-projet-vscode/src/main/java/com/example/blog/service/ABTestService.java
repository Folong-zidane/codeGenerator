package com.example.blog.service;

import com.example.blog.entity.ABTest;
import com.example.blog.repository.ABTestRepository;
import com.example.blog.dto.ABTestCreateDto;
import com.example.blog.dto.ABTestReadDto;
import com.example.blog.dto.ABTestUpdateDto;
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

import com.example.blog.enums.ABTestStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class ABTestService {

    private final ABTestRepository repository;

    @Transactional(readOnly = true)
    public List<ABTestReadDto> findAll() {        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ABTestReadDto> findById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ABTestReadDto getById(Long id) {        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("ABTest", id));
    }

    public ABTestReadDto create(ABTestCreateDto createDto) {        ABTest entity = convertToEntity(createDto);
        ABTest saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ABTestReadDto update(Long id, ABTestUpdateDto updateDto) {        ABTest existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ABTest", id));
        
        updateEntityFromDto(existing, updateDto);
        ABTest updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ABTest", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ABTestReadDto> findAll(Pageable pageable) {        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ABTestReadDto convertToReadDto(ABTest entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ABTestReadDto();
    }

    private ABTest convertToEntity(ABTestCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new ABTest();
    }

    private void updateEntityFromDto(ABTest entity, ABTestUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ABTestReadDto suspendABTest(Long id) {        ABTest entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ABTest", id));
        entity.suspend();
        ABTest updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ABTestReadDto activateABTest(Long id) {        ABTest entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ABTest", id));
        entity.activate();
        ABTest updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
