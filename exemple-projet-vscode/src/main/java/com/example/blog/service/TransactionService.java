package com.example.blog.service;

import com.example.blog.entity.Transaction;
import com.example.blog.repository.TransactionRepository;
import com.example.blog.dto.TransactionCreateDto;
import com.example.blog.dto.TransactionReadDto;
import com.example.blog.dto.TransactionUpdateDto;
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

import com.example.blog.enums.TransactionStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final TransactionRepository repository;

    @Transactional(readOnly = true)
    public List<TransactionReadDto> findAll() {
        log.debug("Finding all Transactions");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TransactionReadDto> findById(Long id) {
        log.debug("Finding Transaction by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public TransactionReadDto getById(Long id) {
        log.debug("Getting Transaction by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Transaction", id));
    }

    public TransactionReadDto create(TransactionCreateDto createDto) {
        log.info("Creating new Transaction: {}", createDto);
        Transaction entity = convertToEntity(createDto);
        Transaction saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public TransactionReadDto update(Long id, TransactionUpdateDto updateDto) {
        log.info("Updating Transaction with id: {}", id);
        Transaction existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transaction", id));
        
        updateEntityFromDto(existing, updateDto);
        Transaction updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Transaction with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Transaction", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Transaction with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private TransactionReadDto convertToReadDto(Transaction entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new TransactionReadDto();
    }

    private Transaction convertToEntity(TransactionCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Transaction();
    }

    private void updateEntityFromDto(Transaction entity, TransactionUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public TransactionReadDto suspendTransaction(Long id) {
        log.info("Suspending Transaction with id: {}", id);
        Transaction entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transaction", id));
        entity.suspend();
        Transaction updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public TransactionReadDto activateTransaction(Long id) {
        log.info("Activating Transaction with id: {}", id);
        Transaction entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transaction", id));
        entity.activate();
        Transaction updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
