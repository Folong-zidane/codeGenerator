package com.example.blog.controller;

import com.example.blog.dto.UserPreferenceCreateDto;
import com.example.blog.dto.UserPreferenceReadDto;
import com.example.blog.dto.UserPreferenceUpdateDto;
import com.example.blog.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/userpreferences")
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceController {

    private final UserPreferenceService service;

    @GetMapping
    public ResponseEntity<Page<UserPreferenceReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/userpreferences - Finding all UserPreferences");
        Page<UserPreferenceReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPreferenceReadDto>> findAllList() {
        log.info("GET /api/v1/userpreferences/all - Finding all UserPreferences as list");
        List<UserPreferenceReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPreferenceReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/userpreferences/{} - Finding UserPreference by id", id);
        UserPreferenceReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserPreferenceReadDto> create(@Valid @RequestBody UserPreferenceCreateDto createDto) {
        log.info("POST /api/v1/userpreferences - Creating new UserPreference: {}", createDto);
        UserPreferenceReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPreferenceReadDto> update(@PathVariable Long id, @Valid @RequestBody UserPreferenceUpdateDto updateDto) {
        log.info("PUT /api/v1/userpreferences/{} - Updating UserPreference: {}", id, updateDto);
        UserPreferenceReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/userpreferences/{} - Deleting UserPreference", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<UserPreferenceReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/userpreferences/{}/suspend - Suspending UserPreference", id);
        UserPreferenceReadDto suspended = service.suspendUserPreference(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserPreferenceReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/userpreferences/{}/activate - Activating UserPreference", id);
        UserPreferenceReadDto activated = service.activateUserPreference(id);
        return ResponseEntity.ok(activated);
    }

}
