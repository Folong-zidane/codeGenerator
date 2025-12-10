package com.example.blog.controller;

import com.example.blog.dto.EditorStateCreateDto;
import com.example.blog.dto.EditorStateReadDto;
import com.example.blog.dto.EditorStateUpdateDto;
import com.example.blog.service.EditorStateService;
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
@RequestMapping("/api/v1/editorstates")
@RequiredArgsConstructor
@Slf4j
public class EditorStateController {

    private final EditorStateService service;

    @GetMapping
    public ResponseEntity<Page<EditorStateReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/editorstates - Finding all EditorStates");
        Page<EditorStateReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EditorStateReadDto>> findAllList() {
        log.info("GET /api/v1/editorstates/all - Finding all EditorStates as list");
        List<EditorStateReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorStateReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/editorstates/{} - Finding EditorState by id", id);
        EditorStateReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EditorStateReadDto> create(@Valid @RequestBody EditorStateCreateDto createDto) {
        log.info("POST /api/v1/editorstates - Creating new EditorState: {}", createDto);
        EditorStateReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditorStateReadDto> update(@PathVariable Long id, @Valid @RequestBody EditorStateUpdateDto updateDto) {
        log.info("PUT /api/v1/editorstates/{} - Updating EditorState: {}", id, updateDto);
        EditorStateReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/editorstates/{} - Deleting EditorState", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<EditorStateReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/editorstates/{}/suspend - Suspending EditorState", id);
        EditorStateReadDto suspended = service.suspendEditorState(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EditorStateReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/editorstates/{}/activate - Activating EditorState", id);
        EditorStateReadDto activated = service.activateEditorState(id);
        return ResponseEntity.ok(activated);
    }

}
