package com.example.blog.controller;

import com.example.blog.dto.AbonnementPayantCreateDto;
import com.example.blog.dto.AbonnementPayantReadDto;
import com.example.blog.dto.AbonnementPayantUpdateDto;
import com.example.blog.service.AbonnementPayantService;
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
@RequestMapping("/api/v1/abonnementpayants")
@RequiredArgsConstructor
@Slf4j
public class AbonnementPayantController {

    private final AbonnementPayantService service;

    @GetMapping
    public ResponseEntity<Page<AbonnementPayantReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/abonnementpayants - Finding all AbonnementPayants");
        Page<AbonnementPayantReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AbonnementPayantReadDto>> findAllList() {
        log.info("GET /api/v1/abonnementpayants/all - Finding all AbonnementPayants as list");
        List<AbonnementPayantReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbonnementPayantReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/abonnementpayants/{} - Finding AbonnementPayant by id", id);
        AbonnementPayantReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AbonnementPayantReadDto> create(@Valid @RequestBody AbonnementPayantCreateDto createDto) {
        log.info("POST /api/v1/abonnementpayants - Creating new AbonnementPayant: {}", createDto);
        AbonnementPayantReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbonnementPayantReadDto> update(@PathVariable Long id, @Valid @RequestBody AbonnementPayantUpdateDto updateDto) {
        log.info("PUT /api/v1/abonnementpayants/{} - Updating AbonnementPayant: {}", id, updateDto);
        AbonnementPayantReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/abonnementpayants/{} - Deleting AbonnementPayant", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AbonnementPayantReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/abonnementpayants/{}/suspend - Suspending AbonnementPayant", id);
        AbonnementPayantReadDto suspended = service.suspendAbonnementPayant(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AbonnementPayantReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/abonnementpayants/{}/activate - Activating AbonnementPayant", id);
        AbonnementPayantReadDto activated = service.activateAbonnementPayant(id);
        return ResponseEntity.ok(activated);
    }

}
