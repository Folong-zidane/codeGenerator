package com.example.blog.controller;

import com.example.blog.dto.BoostRuleCreateDto;
import com.example.blog.dto.BoostRuleReadDto;
import com.example.blog.dto.BoostRuleUpdateDto;
import com.example.blog.service.BoostRuleService;
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
@RequestMapping("/api/v1/boostrules")
@RequiredArgsConstructor
@Slf4j
public class BoostRuleController {

    private final BoostRuleService service;

    @GetMapping
    public ResponseEntity<Page<BoostRuleReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/boostrules - Finding all BoostRules");
        Page<BoostRuleReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BoostRuleReadDto>> findAllList() {
        log.info("GET /api/v1/boostrules/all - Finding all BoostRules as list");
        List<BoostRuleReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoostRuleReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/boostrules/{} - Finding BoostRule by id", id);
        BoostRuleReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BoostRuleReadDto> create(@Valid @RequestBody BoostRuleCreateDto createDto) {
        log.info("POST /api/v1/boostrules - Creating new BoostRule: {}", createDto);
        BoostRuleReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoostRuleReadDto> update(@PathVariable Long id, @Valid @RequestBody BoostRuleUpdateDto updateDto) {
        log.info("PUT /api/v1/boostrules/{} - Updating BoostRule: {}", id, updateDto);
        BoostRuleReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/boostrules/{} - Deleting BoostRule", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<BoostRuleReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/boostrules/{}/suspend - Suspending BoostRule", id);
        BoostRuleReadDto suspended = service.suspendBoostRule(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<BoostRuleReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/boostrules/{}/activate - Activating BoostRule", id);
        BoostRuleReadDto activated = service.activateBoostRule(id);
        return ResponseEntity.ok(activated);
    }

}
