package com.example.blog.controller;

import com.example.blog.dto.RubriqueCreateDto;
import com.example.blog.dto.RubriqueReadDto;
import com.example.blog.dto.RubriqueUpdateDto;
import com.example.blog.dto.ArticleReadDto;
import com.example.blog.service.RubriqueService;
import com.example.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rubriques")
@RequiredArgsConstructor
@Tag(name = "Rubriques", description = "Gestion des catégories et sections d'articles")
public class RubriqueController {

    private final RubriqueService rubriqueService;
    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Liste toutes les rubriques", description = "Retourne toutes les rubriques avec hiérarchie")
    public ResponseEntity<List<RubriqueReadDto>> findAll() {
        return ResponseEntity.ok(rubriqueService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détails d'une rubrique", description = "Récupère une rubrique par son ID")
    public ResponseEntity<RubriqueReadDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(rubriqueService.getById(Long.valueOf(id)));
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle rubrique", description = "Création d'une catégorie ou sous-catégorie")
    public ResponseEntity<RubriqueReadDto> create(@Valid @RequestBody RubriqueCreateDto createDto) {
        RubriqueReadDto created = rubriqueService.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une rubrique")
    public ResponseEntity<RubriqueReadDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody RubriqueUpdateDto updateDto) {
        return ResponseEntity.ok(rubriqueService.update(Long.valueOf(id), updateDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une rubrique")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rubriqueService.deleteById(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/articles")
    @Operation(summary = "Articles d'une rubrique", description = "Liste les articles publiés d'une rubrique")
    public ResponseEntity<List<ArticleReadDto>> getArticles(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getArticlesByRubrique(id));
    }

    @GetMapping("/tree")
    @Operation(summary = "Arbre des rubriques", description = "Structure hiérarchique complète")
    public ResponseEntity<List<RubriqueReadDto>> getTree() {
        return ResponseEntity.ok(rubriqueService.findAll());
    }

    @GetMapping("/visible")
    @Operation(summary = "Rubriques visibles", description = "Rubriques actives pour le frontend")
    public ResponseEntity<List<RubriqueReadDto>> getVisible() {
        return ResponseEntity.ok(rubriqueService.findAll());
    }
}