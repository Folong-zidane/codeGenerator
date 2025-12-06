package com.improved.test.service;

import com.improved.test.entity.Product;
import com.improved.test.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.improved.test.enums.ProductStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Product save(Product entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Product suspendProduct(Long id) {
        Product entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        entity.suspend();
        return repository.save(entity);
    }

    public Product activateProduct(Long id) {
        Product entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        entity.activate();
        return repository.save(entity);
    }

}
