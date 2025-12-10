package com.example.blog.controller;

import com.example.blog.dto.CommentaireCreateDto;
import com.example.blog.dto.CommentaireReadDto;
import com.example.blog.dto.CommentaireUpdateDto;
import com.example.blog.service.CommentaireService;
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
@RequestMapping("/api/v1/commentaires")
@RequiredArgsConstructor
@Slf4j
public class CommentaireController {

    private final CommentaireService service;

    @GetMapping
    public ResponseEntity<Page<CommentaireReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/commentaires - Finding all Commentaires");
        Page<CommentaireReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentaireReadDto>> findAllList() {
        log.info("GET /api/v1/commentaires/all - Finding all Commentaires as list");
        List<CommentaireReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentaireReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/commentaires/{} - Finding Commentaire by id", id);
        CommentaireReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CommentaireReadDto> create(@Valid @RequestBody CommentaireCreateDto createDto) {
        log.info("POST /api/v1/commentaires - Creating new Commentaire: {}", createDto);
        CommentaireReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentaireReadDto> update(@PathVariable Long id, @Valid @RequestBody CommentaireUpdateDto updateDto) {
        log.info("PUT /api/v1/commentaires/{} - Updating Commentaire: {}", id, updateDto);
        CommentaireReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/commentaires/{} - Deleting Commentaire", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CommentaireReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/commentaires/{}/suspend - Suspending Commentaire", id);
        CommentaireReadDto suspended = service.suspendCommentaire(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CommentaireReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/commentaires/{}/activate - Activating Commentaire", id);
        CommentaireReadDto activated = service.activateCommentaire(id);
        return ResponseEntity.ok(activated);
    }

}
