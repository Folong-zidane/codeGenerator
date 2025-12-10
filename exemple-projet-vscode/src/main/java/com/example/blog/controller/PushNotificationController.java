package com.example.blog.controller;

import com.example.blog.dto.PushNotificationCreateDto;
import com.example.blog.dto.PushNotificationReadDto;
import com.example.blog.dto.PushNotificationUpdateDto;
import com.example.blog.service.PushNotificationService;
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
@RequestMapping("/api/v1/pushnotifications")
@RequiredArgsConstructor
@Slf4j
public class PushNotificationController {

    private final PushNotificationService service;

    @GetMapping
    public ResponseEntity<Page<PushNotificationReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/pushnotifications - Finding all PushNotifications");
        Page<PushNotificationReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PushNotificationReadDto>> findAllList() {
        log.info("GET /api/v1/pushnotifications/all - Finding all PushNotifications as list");
        List<PushNotificationReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PushNotificationReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/pushnotifications/{} - Finding PushNotification by id", id);
        PushNotificationReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PushNotificationReadDto> create(@Valid @RequestBody PushNotificationCreateDto createDto) {
        log.info("POST /api/v1/pushnotifications - Creating new PushNotification: {}", createDto);
        PushNotificationReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PushNotificationReadDto> update(@PathVariable Long id, @Valid @RequestBody PushNotificationUpdateDto updateDto) {
        log.info("PUT /api/v1/pushnotifications/{} - Updating PushNotification: {}", id, updateDto);
        PushNotificationReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/pushnotifications/{} - Deleting PushNotification", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<PushNotificationReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/pushnotifications/{}/suspend - Suspending PushNotification", id);
        PushNotificationReadDto suspended = service.suspendPushNotification(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PushNotificationReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/pushnotifications/{}/activate - Activating PushNotification", id);
        PushNotificationReadDto activated = service.activatePushNotification(id);
        return ResponseEntity.ok(activated);
    }

}
