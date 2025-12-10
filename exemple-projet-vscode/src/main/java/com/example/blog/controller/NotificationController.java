package com.example.blog.controller;

import com.example.blog.dto.NotificationCreateDto;
import com.example.blog.dto.NotificationReadDto;
import com.example.blog.dto.NotificationUpdateDto;
import com.example.blog.service.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<Page<NotificationReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/notifications - Finding all Notifications");
        Page<NotificationReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationReadDto>> findAllList() {
        log.info("GET /api/v1/notifications/all - Finding all Notifications as list");
        List<NotificationReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/notifications/{} - Finding Notification by id", id);
        NotificationReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<NotificationReadDto> create(@Valid @RequestBody NotificationCreateDto createDto) {
        log.info("POST /api/v1/notifications - Creating new Notification: {}", createDto);
        NotificationReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationReadDto> update(@PathVariable Long id, @Valid @RequestBody NotificationUpdateDto updateDto) {
        log.info("PUT /api/v1/notifications/{} - Updating Notification: {}", id, updateDto);
        NotificationReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/notifications/{} - Deleting Notification", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<NotificationReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/notifications/{}/suspend - Suspending Notification", id);
        NotificationReadDto suspended = service.suspendNotification(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<NotificationReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/notifications/{}/activate - Activating Notification", id);
        NotificationReadDto activated = service.activateNotification(id);
        return ResponseEntity.ok(activated);
    }

}
