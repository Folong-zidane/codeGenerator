package com.example.blog.controller;

import com.example.blog.dto.TicketSupportCreateDto;
import com.example.blog.dto.TicketSupportReadDto;
import com.example.blog.dto.TicketSupportUpdateDto;
import com.example.blog.service.TicketSupportService;
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
@RequestMapping("/api/v1/ticketsupports")
@RequiredArgsConstructor
@Slf4j
public class TicketSupportController {

    private final TicketSupportService service;

    @GetMapping
    public ResponseEntity<Page<TicketSupportReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/ticketsupports - Finding all TicketSupports");
        Page<TicketSupportReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketSupportReadDto>> findAllList() {
        log.info("GET /api/v1/ticketsupports/all - Finding all TicketSupports as list");
        List<TicketSupportReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketSupportReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/ticketsupports/{} - Finding TicketSupport by id", id);
        TicketSupportReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TicketSupportReadDto> create(@Valid @RequestBody TicketSupportCreateDto createDto) {
        log.info("POST /api/v1/ticketsupports - Creating new TicketSupport: {}", createDto);
        TicketSupportReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketSupportReadDto> update(@PathVariable Long id, @Valid @RequestBody TicketSupportUpdateDto updateDto) {
        log.info("PUT /api/v1/ticketsupports/{} - Updating TicketSupport: {}", id, updateDto);
        TicketSupportReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/ticketsupports/{} - Deleting TicketSupport", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<TicketSupportReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/ticketsupports/{}/suspend - Suspending TicketSupport", id);
        TicketSupportReadDto suspended = service.suspendTicketSupport(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<TicketSupportReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/ticketsupports/{}/activate - Activating TicketSupport", id);
        TicketSupportReadDto activated = service.activateTicketSupport(id);
        return ResponseEntity.ok(activated);
    }

}
