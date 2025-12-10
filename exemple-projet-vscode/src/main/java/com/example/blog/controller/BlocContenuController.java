package com.example.blog.controller;

import com.example.blog.dto.BlocContenuCreateDto;
import com.example.blog.dto.BlocContenuReadDto;
import com.example.blog.dto.BlocContenuUpdateDto;
import com.example.blog.service.BlocContenuService;
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
@RequestMapping("/api/v1/bloccontenus")
@RequiredArgsConstructor
@Slf4j
public class BlocContenuController {

    private final BlocContenuService service;

    @GetMapping
    public ResponseEntity<Page<BlocContenuReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/bloccontenus - Finding all BlocContenus");
        Page<BlocContenuReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BlocContenuReadDto>> findAllList() {
        log.info("GET /api/v1/bloccontenus/all - Finding all BlocContenus as list");
        List<BlocContenuReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlocContenuReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/bloccontenus/{} - Finding BlocContenu by id", id);
        BlocContenuReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BlocContenuReadDto> create(@Valid @RequestBody BlocContenuCreateDto createDto) {
        log.info("POST /api/v1/bloccontenus - Creating new BlocContenu: {}", createDto);
        BlocContenuReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlocContenuReadDto> update(@PathVariable Long id, @Valid @RequestBody BlocContenuUpdateDto updateDto) {
        log.info("PUT /api/v1/bloccontenus/{} - Updating BlocContenu: {}", id, updateDto);
        BlocContenuReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/bloccontenus/{} - Deleting BlocContenu", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<BlocContenuReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/bloccontenus/{}/suspend - Suspending BlocContenu", id);
        BlocContenuReadDto suspended = service.suspendBlocContenu(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<BlocContenuReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/bloccontenus/{}/activate - Activating BlocContenu", id);
        BlocContenuReadDto activated = service.activateBlocContenu(id);
        return ResponseEntity.ok(activated);
    }

}
