package com.example.blog.controller;

import com.example.blog.dto.UserCreateDto;
import com.example.blog.dto.UserReadDto;
import com.example.blog.dto.UserUpdateDto;
import com.example.blog.service.UserService;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Page<UserReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/users - Finding all Users");
        Page<UserReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserReadDto>> findAllList() {
        log.info("GET /api/v1/users/all - Finding all Users as list");
        List<UserReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/users/{} - Finding User by id", id);
        UserReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserReadDto> create(@Valid @RequestBody UserCreateDto createDto) {
        log.info("POST /api/v1/users - Creating new User: {}", createDto);
        UserReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReadDto> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        log.info("PUT /api/v1/users/{} - Updating User: {}", id, updateDto);
        UserReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/users/{} - Deleting User", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<UserReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/users/{}/suspend - Suspending User", id);
        UserReadDto suspended = service.suspendUser(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/users/{}/activate - Activating User", id);
        UserReadDto activated = service.activateUser(id);
        return ResponseEntity.ok(activated);
    }

}
