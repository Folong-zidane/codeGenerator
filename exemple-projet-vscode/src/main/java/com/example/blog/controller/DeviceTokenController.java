package com.example.blog.controller;

import com.example.blog.dto.DeviceTokenCreateDto;
import com.example.blog.dto.DeviceTokenReadDto;
import com.example.blog.dto.DeviceTokenUpdateDto;
import com.example.blog.service.DeviceTokenService;
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
@RequestMapping("/api/v1/devicetokens")
@RequiredArgsConstructor
@Slf4j
public class DeviceTokenController {

    private final DeviceTokenService service;

    @GetMapping
    public ResponseEntity<Page<DeviceTokenReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/devicetokens - Finding all DeviceTokens");
        Page<DeviceTokenReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceTokenReadDto>> findAllList() {
        log.info("GET /api/v1/devicetokens/all - Finding all DeviceTokens as list");
        List<DeviceTokenReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceTokenReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/devicetokens/{} - Finding DeviceToken by id", id);
        DeviceTokenReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DeviceTokenReadDto> create(@Valid @RequestBody DeviceTokenCreateDto createDto) {
        log.info("POST /api/v1/devicetokens - Creating new DeviceToken: {}", createDto);
        DeviceTokenReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceTokenReadDto> update(@PathVariable Long id, @Valid @RequestBody DeviceTokenUpdateDto updateDto) {
        log.info("PUT /api/v1/devicetokens/{} - Updating DeviceToken: {}", id, updateDto);
        DeviceTokenReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/devicetokens/{} - Deleting DeviceToken", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<DeviceTokenReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/devicetokens/{}/suspend - Suspending DeviceToken", id);
        DeviceTokenReadDto suspended = service.suspendDeviceToken(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<DeviceTokenReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/devicetokens/{}/activate - Activating DeviceToken", id);
        DeviceTokenReadDto activated = service.activateDeviceToken(id);
        return ResponseEntity.ok(activated);
    }

}
