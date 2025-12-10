package com.example.blog.controller;

import com.example.blog.dto.NotificationPreferenceCreateDto;
import com.example.blog.dto.NotificationPreferenceReadDto;
import com.example.blog.dto.NotificationPreferenceUpdateDto;
import com.example.blog.service.NotificationPreferenceService;
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
@RequestMapping("/api/v1/notificationpreferences")
@RequiredArgsConstructor
@Slf4j
public class NotificationPreferenceController {

    private final NotificationPreferenceService service;

    @GetMapping
    public ResponseEntity<Page<NotificationPreferenceReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/notificationpreferences - Finding all NotificationPreferences");
        Page<NotificationPreferenceReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationPreferenceReadDto>> findAllList() {
        log.info("GET /api/v1/notificationpreferences/all - Finding all NotificationPreferences as list");
        List<NotificationPreferenceReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationPreferenceReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/notificationpreferences/{} - Finding NotificationPreference by id", id);
        NotificationPreferenceReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<NotificationPreferenceReadDto> create(@Valid @RequestBody NotificationPreferenceCreateDto createDto) {
        log.info("POST /api/v1/notificationpreferences - Creating new NotificationPreference: {}", createDto);
        NotificationPreferenceReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationPreferenceReadDto> update(@PathVariable Long id, @Valid @RequestBody NotificationPreferenceUpdateDto updateDto) {
        log.info("PUT /api/v1/notificationpreferences/{} - Updating NotificationPreference: {}", id, updateDto);
        NotificationPreferenceReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/notificationpreferences/{} - Deleting NotificationPreference", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<NotificationPreferenceReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/notificationpreferences/{}/suspend - Suspending NotificationPreference", id);
        NotificationPreferenceReadDto suspended = service.suspendNotificationPreference(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<NotificationPreferenceReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/notificationpreferences/{}/activate - Activating NotificationPreference", id);
        NotificationPreferenceReadDto activated = service.activateNotificationPreference(id);
        return ResponseEntity.ok(activated);
    }

}
