package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Payment;
import com.ecommerce.complex.repository.PaymentRepository;
import com.ecommerce.complex.dto.PaymentCreateDto;
import com.ecommerce.complex.dto.PaymentReadDto;
import com.ecommerce.complex.dto.PaymentUpdateDto;
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

import com.ecommerce.complex.enums.PaymentStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private final PaymentRepository repository;

    @Transactional(readOnly = true)
    public List<PaymentReadDto> findAll() {
        log.debug("Finding all Payments");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PaymentReadDto> findById(Long id) {
        log.debug("Finding Payment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public PaymentReadDto getById(Long id) {
        log.debug("Getting Payment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Payment", id));
    }

    public PaymentReadDto create(PaymentCreateDto createDto) {
        log.info("Creating new Payment: {}", createDto);
        Payment entity = convertToEntity(createDto);
        Payment saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public PaymentReadDto update(Long id, PaymentUpdateDto updateDto) {
        log.info("Updating Payment with id: {}", id);
        Payment existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment", id));
        
        updateEntityFromDto(existing, updateDto);
        Payment updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Payment with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Payment", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PaymentReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Payment with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private PaymentReadDto convertToReadDto(Payment entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new PaymentReadDto();
    }

    private Payment convertToEntity(PaymentCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Payment();
    }

    private void updateEntityFromDto(Payment entity, PaymentUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public PaymentReadDto suspendPayment(Long id) {
        log.info("Suspending Payment with id: {}", id);
        Payment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment", id));
        entity.suspend();
        Payment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public PaymentReadDto activatePayment(Long id) {
        log.info("Activating Payment with id: {}", id);
        Payment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Payment", id));
        entity.activate();
        Payment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
