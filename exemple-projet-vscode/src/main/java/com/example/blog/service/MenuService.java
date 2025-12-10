package com.example.blog.service;

import com.example.blog.entity.Menu;
import com.example.blog.repository.MenuRepository;
import com.example.blog.dto.MenuCreateDto;
import com.example.blog.dto.MenuReadDto;
import com.example.blog.dto.MenuUpdateDto;
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

import com.example.blog.enums.MenuStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MenuService {

    private final MenuRepository repository;

    @Transactional(readOnly = true)
    public List<MenuReadDto> findAll() {
        log.debug("Finding all Menus");
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MenuReadDto> findById(Long id) {
        log.debug("Finding Menu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public MenuReadDto getById(Long id) {
        log.debug("Getting Menu by id: {}", id);
        return repository.findById(id)
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Menu", id));
    }

    public MenuReadDto create(MenuCreateDto createDto) {
        log.info("Creating new Menu: {}", createDto);
        Menu entity = convertToEntity(createDto);
        Menu saved = repository.save(entity);
        return convertToReadDto(saved);
    }

    public MenuReadDto update(Long id, MenuUpdateDto updateDto) {
        log.info("Updating Menu with id: {}", id);
        Menu existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu", id));
        
        updateEntityFromDto(existing, updateDto);
        Menu updated = repository.save(existing);
        return convertToReadDto(updated);
    }

    public void deleteById(Long id) {
        log.info("Deleting Menu with id: {}", id);
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Menu", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<MenuReadDto> findAll(Pageable pageable) {
        log.debug("Finding all Menu with pagination");
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }

    // DTO Conversion Methods
    private MenuReadDto convertToReadDto(Menu entity) {
        // TODO: Implement mapping logic or use MapStruct
        return new MenuReadDto();
    }

    private Menu convertToEntity(MenuCreateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
        return new Menu();
    }

    private void updateEntityFromDto(Menu entity, MenuUpdateDto dto) {
        // TODO: Implement mapping logic or use MapStruct
    }

    public MenuReadDto suspendMenu(Long id) {
        log.info("Suspending Menu with id: {}", id);
        Menu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu", id));
        entity.suspend();
        Menu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

    public MenuReadDto activateMenu(Long id) {
        log.info("Activating Menu with id: {}", id);
        Menu entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu", id));
        entity.activate();
        Menu updated = repository.save(entity);
        return convertToReadDto(updated);
    }

}
