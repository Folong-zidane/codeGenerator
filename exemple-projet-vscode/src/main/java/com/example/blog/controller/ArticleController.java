package com.example.blog.controller;

import com.example.blog.dto.ArticleCreateDto;
import com.example.blog.dto.ArticleReadDto;
import com.example.blog.dto.ArticleUpdateDto;
import com.example.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import com.example.blog.dto.ArticlePublicationDto;
import com.example.blog.dto.FeaturedArticleDto;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Gestion des articles de la plateforme interculturelle")
public class ArticleController {

    private final ArticleService service;

    @GetMapping
    @Operation(summary = "Liste tous les articles avec pagination", description = "Retourne une page d'articles")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    public ResponseEntity<Page<ArticleReadDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Liste tous les articles", description = "Retourne tous les articles sans pagination")
    public ResponseEntity<List<ArticleReadDto>> findAllList() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un article par ID", description = "Retourne les détails complets d'un article")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article trouvé"),
        @ApiResponse(responseCode = "404", description = "Article non trouvé")
    })
    public ResponseEntity<ArticleReadDto> findById(@PathVariable @Parameter(description = "ID de l'article", required = true) Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crée un nouvel article", description = "Crée un article avec blocs de contenu modulaires (texte, image, vidéo, PDF, etc.)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Article créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Rubrique ou auteur non trouvé")
    })
    public ResponseEntity<ArticleReadDto> create(
            @Valid @RequestBody @Parameter(description = "Données de l'article à créer", required = true) ArticleCreateDto createDto) {
        ArticleReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un article", description = "Modifie les informations d'un article existant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article mis à jour"),
        @ApiResponse(responseCode = "404", description = "Article non trouvé")
    })
    public ResponseEntity<ArticleReadDto> update(
            @PathVariable @Parameter(description = "ID de l'article", required = true) Integer id,
            @Valid @RequestBody @Parameter(description = "Données à mettre à jour", required = true) ArticleUpdateDto updateDto) {
        return ResponseEntity.ok(service.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un article", description = "Suppression définitive d'un article")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Article supprimé"),
        @ApiResponse(responseCode = "404", description = "Article non trouvé")
    })
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "ID de l'article", required = true) Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publie un article", description = "Change le statut d'un article en PUBLIE et définit la date de publication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Article publié"),
        @ApiResponse(responseCode = "404", description = "Article non trouvé")
    })
    public ResponseEntity<ArticleReadDto> publish(@PathVariable @Parameter(description = "ID de l'article", required = true) Integer id) {
        return ResponseEntity.ok(service.publishArticle(id));
    }

    @PostMapping("/{id}/publish-advanced")
    @Operation(summary = "Publication avancée d'un article", description = "Publication avec options avancées (programmation, avant-première, etc.)")
    public ResponseEntity<ArticleReadDto> publishAdvanced(
            @PathVariable Integer id,
            @Valid @RequestBody ArticlePublicationDto publicationDto) {
        return ResponseEntity.ok(service.publishAdvanced(id, publicationDto));
    }

    @PatchMapping("/{id}/schedule")
    @Operation(summary = "Programme la publication d'un article", description = "Définit une date de publication future")
    public ResponseEntity<ArticleReadDto> schedule(
            @PathVariable Integer id,
            @RequestParam LocalDateTime datePublication) {
        return ResponseEntity.ok(service.scheduleArticle(id, datePublication));
    }

    @PatchMapping("/{id}/preview")
    @Operation(summary = "Met en avant-première", description = "Active l'avant-première pour un article")
    public ResponseEntity<ArticleReadDto> setPreview(
            @PathVariable Integer id,
            @RequestParam(required = false) LocalDateTime dateFin) {
        return ResponseEntity.ok(service.setPreview(id, dateFin));
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archive un article", description = "Change le statut en ARCHIVE")
    public ResponseEntity<ArticleReadDto> archive(@PathVariable Integer id) {
        return ResponseEntity.ok(service.archiveArticle(id));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Rejette un article", description = "Rejette un article avec motif")
    public ResponseEntity<ArticleReadDto> reject(
            @PathVariable Integer id,
            @RequestParam String motif) {
        return ResponseEntity.ok(service.rejectArticle(id, motif));
    }

    @PostMapping("/{id}/feature")
    @Operation(summary = "Met en avant un article", description = "Ajoute l'article aux contenus mis en avant")
    public ResponseEntity<Void> feature(
            @PathVariable Integer id,
            @Valid @RequestBody FeaturedArticleDto featuredDto) {
        service.featureArticle(id, featuredDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/featured")
    @Operation(summary = "Articles en avant", description = "Récupère les articles mis en avant par section")
    public ResponseEntity<List<ArticleReadDto>> getFeatured(
            @RequestParam(required = false) String section) {
        return ResponseEntity.ok(service.getFeaturedArticles(section));
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Articles par statut", description = "Récupère les articles selon leur statut")
    public ResponseEntity<Page<ArticleReadDto>> getByStatus(
            @PathVariable String status,
            Pageable pageable) {
        return ResponseEntity.ok(service.getArticlesByStatus(status, pageable));
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Articles programmés", description = "Liste des articles programmés pour publication")
    public ResponseEntity<List<ArticleReadDto>> getScheduled() {
        return ResponseEntity.ok(service.getScheduledArticles());
    }

}
