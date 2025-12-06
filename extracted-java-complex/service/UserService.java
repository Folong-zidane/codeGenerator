package com.ecommerce.complex.service;

import com.ecommerce.complex.entity.User;
import com.ecommerce.complex.repository.UserRepository;
import com.ecommerce.complex.dto.UserCreateDto;
import com.ecommerce.complex.dto.UserReadDto;
import com.ecommerce.complex.dto.UserUpdateDto;
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

import com.ecommerce.complex.enums.UserStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository repository;

    @Transactional(readOnly = true)
    public List<UserReadDto> findAll() {
        log.debug("Finding all Users");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserReadDto> findById(Long id) {
        log.debug("Finding User by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public UserReadDto getById(Long id) {
        log.debug("Getting User by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    public UserReadDto create(UserCreateDto createDto) {
        log.info("Creating new User: {}", createDto);
        User entity = convertToEntity(createDto);
        User saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public UserReadDto update(Long id, UserUpdateDto updateDto) {
        log.info("Updating User with id: {}", id);
        User existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User", id));
        
        updateEntityFromDto(existing, updateDto);
        User updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting User with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UserReadDto> findAll(Pageable pageable) {
        log.debug("Finding all User with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private UserReadDto convertToReadDto(User entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new UserReadDto();
    }

    private User convertToEntity(UserCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new User();
    }

    private void updateEntityFromDto(User entity, UserUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public UserReadDto suspendUser(Long id) {
        log.info("Suspending User with id: {}", id);
        User entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User", id));
        entity.suspend();
        User updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public UserReadDto activateUser(Long id) {
        log.info("Activating User with id: {}", id);
        User entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User", id));
        entity.activate();
        User updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
