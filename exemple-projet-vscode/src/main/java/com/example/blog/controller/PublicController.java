package com.example.blog.controller;

import com.example.blog.dto.ArticleReadDto;
import com.example.blog.dto.RubriqueReadDto;
import com.example.blog.service.ArticleService;
import com.example.blog.service.RubriqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Endpoints publics accessibles sans authentification")
public class PublicController {

    private final ArticleService articleService;
    private final RubriqueService rubriqueService;

    @GetMapping("/articles")
    @Operation(summary = "Articles publics", description = "Liste des articles publiés accessibles à tous")
    public ResponseEntity<Page<ArticleReadDto>> getPublicArticles(Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesByStatus("PUBLIE", pageable));
    }

    @GetMapping("/articles/{id}")
    @Operation(summary = "Article public", description = "Détails d'un article publié")
    public ResponseEntity<ArticleReadDto> getPublicArticle(@PathVariable Integer id) {
        ArticleReadDto article = articleService.getById(id);
        if (!"PUBLIE".equals(article.getStatut())) {
            throw new RuntimeException("Article non accessible");
        }
        return ResponseEntity.ok(article);
    }

    @GetMapping("/articles/featured")
    @Operation(summary = "Articles en avant publics", description = "Articles mis en avant visibles publiquement")
    public ResponseEntity<List<ArticleReadDto>> getFeaturedArticles() {
        return ResponseEntity.ok(articleService.getFeaturedArticles(null));
    }

    @GetMapping("/rubriques")
    @Operation(summary = "Rubriques publiques", description = "Liste des catégories visibles")
    public ResponseEntity<List<RubriqueReadDto>> getPublicRubriques() {
        return ResponseEntity.ok(rubriqueService.findAll());
    }

    @GetMapping("/rubriques/{id}/articles")
    @Operation(summary = "Articles par rubrique", description = "Articles publiés d'une catégorie")
    public ResponseEntity<List<ArticleReadDto>> getArticlesByRubrique(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getArticlesByRubrique(id));
    }
}