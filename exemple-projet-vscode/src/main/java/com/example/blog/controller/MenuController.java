package com.example.blog.controller;

import com.example.blog.dto.MenuCreateDto;
import com.example.blog.dto.MenuReadDto;
import com.example.blog.dto.MenuUpdateDto;
import com.example.blog.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService service;

    @GetMapping
    public ResponseEntity<Page<MenuReadDto>> findAll(Pageable pageable) {        Page<MenuReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuReadDto>> findAllList() {        List<MenuReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuReadDto> findById(@PathVariable Long id) {        MenuReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MenuReadDto> create(@Valid @RequestBody MenuCreateDto createDto) {        MenuReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuReadDto> update(@PathVariable Long id, @Valid @RequestBody MenuUpdateDto updateDto) {        MenuReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<MenuReadDto> suspend(@PathVariable Long id) {        MenuReadDto suspended = service.suspendMenu(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<MenuReadDto> activate(@PathVariable Long id) {        MenuReadDto activated = service.activateMenu(id);
        return ResponseEntity.ok(activated);
    }

}
