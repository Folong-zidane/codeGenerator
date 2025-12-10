package com.example.blog.controller;

import com.example.blog.dto.EmailNotificationCreateDto;
import com.example.blog.dto.EmailNotificationReadDto;
import com.example.blog.dto.EmailNotificationUpdateDto;
import com.example.blog.service.EmailNotificationService;
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
@RequestMapping("/api/v1/emailnotifications")
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationController {

    private final EmailNotificationService service;

    @GetMapping
    public ResponseEntity<Page<EmailNotificationReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/emailnotifications - Finding all EmailNotifications");
        Page<EmailNotificationReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmailNotificationReadDto>> findAllList() {
        log.info("GET /api/v1/emailnotifications/all - Finding all EmailNotifications as list");
        List<EmailNotificationReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailNotificationReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/emailnotifications/{} - Finding EmailNotification by id", id);
        EmailNotificationReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EmailNotificationReadDto> create(@Valid @RequestBody EmailNotificationCreateDto createDto) {
        log.info("POST /api/v1/emailnotifications - Creating new EmailNotification: {}", createDto);
        EmailNotificationReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailNotificationReadDto> update(@PathVariable Long id, @Valid @RequestBody EmailNotificationUpdateDto updateDto) {
        log.info("PUT /api/v1/emailnotifications/{} - Updating EmailNotification: {}", id, updateDto);
        EmailNotificationReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/emailnotifications/{} - Deleting EmailNotification", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<EmailNotificationReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/emailnotifications/{}/suspend - Suspending EmailNotification", id);
        EmailNotificationReadDto suspended = service.suspendEmailNotification(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EmailNotificationReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/emailnotifications/{}/activate - Activating EmailNotification", id);
        EmailNotificationReadDto activated = service.activateEmailNotification(id);
        return ResponseEntity.ok(activated);
    }

}
