package com.test.behavioral.service;

import com.test.behavioral.entity.User;
import com.test.behavioral.repository.UserRepository;
import com.test.behavioral.dto.UserCreateDto;
import com.test.behavioral.dto.UserReadDto;
import com.test.behavioral.dto.UserUpdateDto;
import com.test.behavioral.exception.EntityNotFoundException;
import com.test.behavioral.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void createUser() {
        // Generated from sequence diagram
        // Méthode extraite du diagramme de séquence
        log.info("Exécution de createUser");
    }

    public void save() {
        // Generated from sequence diagram
        // Méthode extraite du diagramme de séquence
        log.info("Exécution de save");
    }

}
