package com.example.blog.controller;

import com.example.blog.dto.ContratMaintenanceCreateDto;
import com.example.blog.dto.ContratMaintenanceReadDto;
import com.example.blog.dto.ContratMaintenanceUpdateDto;
import com.example.blog.service.ContratMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contratmaintenances")
@RequiredArgsConstructor
public class ContratMaintenanceController {

    private final ContratMaintenanceService service;

    @GetMapping
    public ResponseEntity<Page<ContratMaintenanceReadDto>> findAll(Pageable pageable) {        Page<ContratMaintenanceReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContratMaintenanceReadDto>> findAllList() {        List<ContratMaintenanceReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratMaintenanceReadDto> findById(@PathVariable Long id) {        ContratMaintenanceReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContratMaintenanceReadDto> create(@Valid @RequestBody ContratMaintenanceCreateDto createDto) {        ContratMaintenanceReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratMaintenanceReadDto> update(@PathVariable Long id, @Valid @RequestBody ContratMaintenanceUpdateDto updateDto) {        ContratMaintenanceReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ContratMaintenanceReadDto> suspend(@PathVariable Long id) {        ContratMaintenanceReadDto suspended = service.suspendContratMaintenance(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ContratMaintenanceReadDto> activate(@PathVariable Long id) {        ContratMaintenanceReadDto activated = service.activateContratMaintenance(id);
        return ResponseEntity.ok(activated);
    }

}
