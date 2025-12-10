package com.example.blog.controller;

import com.example.blog.dto.ABTestCreateDto;
import com.example.blog.dto.ABTestReadDto;
import com.example.blog.dto.ABTestUpdateDto;
import com.example.blog.service.ABTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/abtests")
@RequiredArgsConstructor
public class ABTestController {

    private final ABTestService service;

    @GetMapping
    public ResponseEntity<Page<ABTestReadDto>> findAll(Pageable pageable) {        Page<ABTestReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ABTestReadDto>> findAllList() {        List<ABTestReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ABTestReadDto> findById(@PathVariable Long id) {        ABTestReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ABTestReadDto> create(@Valid @RequestBody ABTestCreateDto createDto) {        ABTestReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ABTestReadDto> update(@PathVariable Long id, @Valid @RequestBody ABTestUpdateDto updateDto) {        ABTestReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ABTestReadDto> suspend(@PathVariable Long id) {        ABTestReadDto suspended = service.suspendABTest(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ABTestReadDto> activate(@PathVariable Long id) {        ABTestReadDto activated = service.activateABTest(id);
        return ResponseEntity.ok(activated);
    }

}
