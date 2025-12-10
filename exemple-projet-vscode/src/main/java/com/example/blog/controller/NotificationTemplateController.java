package com.example.blog.controller;

import com.example.blog.dto.NotificationTemplateCreateDto;
import com.example.blog.dto.NotificationTemplateReadDto;
import com.example.blog.dto.NotificationTemplateUpdateDto;
import com.example.blog.service.NotificationTemplateService;
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
@RequestMapping("/api/v1/notificationtemplates")
@RequiredArgsConstructor
@Slf4j
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    @GetMapping
    public ResponseEntity<Page<NotificationTemplateReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/notificationtemplates - Finding all NotificationTemplates");
        Page<NotificationTemplateReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationTemplateReadDto>> findAllList() {
        log.info("GET /api/v1/notificationtemplates/all - Finding all NotificationTemplates as list");
        List<NotificationTemplateReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationTemplateReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/notificationtemplates/{} - Finding NotificationTemplate by id", id);
        NotificationTemplateReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<NotificationTemplateReadDto> create(@Valid @RequestBody NotificationTemplateCreateDto createDto) {
        log.info("POST /api/v1/notificationtemplates - Creating new NotificationTemplate: {}", createDto);
        NotificationTemplateReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationTemplateReadDto> update(@PathVariable Long id, @Valid @RequestBody NotificationTemplateUpdateDto updateDto) {
        log.info("PUT /api/v1/notificationtemplates/{} - Updating NotificationTemplate: {}", id, updateDto);
        NotificationTemplateReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/notificationtemplates/{} - Deleting NotificationTemplate", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<NotificationTemplateReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/notificationtemplates/{}/suspend - Suspending NotificationTemplate", id);
        NotificationTemplateReadDto suspended = service.suspendNotificationTemplate(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<NotificationTemplateReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/notificationtemplates/{}/activate - Activating NotificationTemplate", id);
        NotificationTemplateReadDto activated = service.activateNotificationTemplate(id);
        return ResponseEntity.ok(activated);
    }

}
