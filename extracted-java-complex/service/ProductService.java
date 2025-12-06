package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.Product;
import com.ecommerce.complex.repository.ProductRepository;
import com.ecommerce.complex.dto.ProductCreateDto;
import com.ecommerce.complex.dto.ProductReadDto;
import com.ecommerce.complex.dto.ProductUpdateDto;
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

import com.ecommerce.complex.enums.ProductStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<ProductReadDto> findAll() {
        log.debug("Finding all Products");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductReadDto> findById(Long id) {
        log.debug("Finding Product by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public ProductReadDto getById(Long id) {
        log.debug("Getting Product by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    public ProductReadDto create(ProductCreateDto createDto) {
        log.info("Creating new Product: {}", createDto);
        Product entity = convertToEntity(createDto);
        Product saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public ProductReadDto update(Long id, ProductUpdateDto updateDto) {
        log.info("Updating Product with id: {}", id);
        Product existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));
        
        updateEntityFromDto(existing, updateDto);
        Product updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Product with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProductReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Product with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private ProductReadDto convertToReadDto(Product entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new ProductReadDto();
    }

    private Product convertToEntity(ProductCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Product();
    }

    private void updateEntityFromDto(Product entity, ProductUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public ProductReadDto suspendProduct(Long id) {
        log.info("Suspending Product with id: {}", id);
        Product entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));
        entity.suspend();
        Product updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public ProductReadDto activateProduct(Long id) {
        log.info("Activating Product with id: {}", id);
        Product entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product", id));
        entity.activate();
        Product updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
