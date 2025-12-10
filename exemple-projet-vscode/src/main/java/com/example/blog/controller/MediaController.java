package com.example.blog.controller;

import com.example.blog.dto.MediaFileReadDto;
import com.example.blog.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Tag(name = "Médias", description = "Upload et gestion des fichiers multimédias")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    @Operation(summary = "Upload un fichier média", description = "Upload image, vidéo, PDF ou audio avec détection d'unicité par hash SHA256")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Fichier uploadé avec succès"),
        @ApiResponse(responseCode = "409", description = "Fichier déjà existant (même hash)"),
        @ApiResponse(responseCode = "400", description = "Type de fichier non supporté")
    })
    public ResponseEntity<MediaFileReadDto> uploadFile(
            @RequestParam("file") @Parameter(description = "Fichier à uploader", required = true) MultipartFile file,
            @RequestParam(value = "legende", required = false) @Parameter(description = "Légende du fichier") String legende,
            @RequestParam(value = "altText", required = false) @Parameter(description = "Texte alternatif") String altText) {
        
        MediaFileReadDto result = mediaService.uploadFile(file, legende, altText);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère les détails d'un média", description = "Retourne les informations et variantes d'un fichier média")
    public ResponseEntity<MediaFileReadDto> getMedia(@PathVariable UUID id) {
        return ResponseEntity.ok(mediaService.getById(id));
    }

    @GetMapping("/hash/{hash}")
    @Operation(summary = "Vérifie l'existence d'un fichier par hash", description = "Permet de vérifier si un fichier existe déjà avant upload")
    public ResponseEntity<MediaFileReadDto> getByHash(@PathVariable String hash) {
        return ResponseEntity.ok(mediaService.getByHash(hash));
    }
}