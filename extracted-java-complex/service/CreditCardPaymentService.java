package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.CreditCardPayment;
import com.ecommerce.complex.repository.CreditCardPaymentRepository;
import com.ecommerce.complex.dto.CreditCardPaymentCreateDto;
import com.ecommerce.complex.dto.CreditCardPaymentReadDto;
import com.ecommerce.complex.dto.CreditCardPaymentUpdateDto;
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

import com.ecommerce.complex.enums.CreditCardPaymentStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreditCardPaymentService {

    private final CreditCardPaymentRepository repository;

    @Transactional(readOnly = true)
    public List<CreditCardPaymentReadDto> findAll() {
        log.debug("Finding all CreditCardPayments");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CreditCardPaymentReadDto> findById(Long id) {
        log.debug("Finding CreditCardPayment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public CreditCardPaymentReadDto getById(Long id) {
        log.debug("Getting CreditCardPayment by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("CreditCardPayment", id));
    }

    public CreditCardPaymentReadDto create(CreditCardPaymentCreateDto createDto) {
        log.info("Creating new CreditCardPayment: {}", createDto);
        CreditCardPayment entity = convertToEntity(createDto);
        CreditCardPayment saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public CreditCardPaymentReadDto update(Long id, CreditCardPaymentUpdateDto updateDto) {
        log.info("Updating CreditCardPayment with id: {}", id);
        CreditCardPayment existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CreditCardPayment", id));
        
        updateEntityFromDto(existing, updateDto);
        CreditCardPayment updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting CreditCardPayment with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("CreditCardPayment", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<CreditCardPaymentReadDto> findAll(Pageable pageable) {
        log.debug("Finding all CreditCardPayment with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private CreditCardPaymentReadDto convertToReadDto(CreditCardPayment entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new CreditCardPaymentReadDto();
    }

    private CreditCardPayment convertToEntity(CreditCardPaymentCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new CreditCardPayment();
    }

    private void updateEntityFromDto(CreditCardPayment entity, CreditCardPaymentUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public CreditCardPaymentReadDto suspendCreditCardPayment(Long id) {
        log.info("Suspending CreditCardPayment with id: {}", id);
        CreditCardPayment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CreditCardPayment", id));
        entity.suspend();
        CreditCardPayment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public CreditCardPaymentReadDto activateCreditCardPayment(Long id) {
        log.info("Activating CreditCardPayment with id: {}", id);
        CreditCardPayment entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CreditCardPayment", id));
        entity.activate();
        CreditCardPayment updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
