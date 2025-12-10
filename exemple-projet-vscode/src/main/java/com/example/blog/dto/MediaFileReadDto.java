package com.example.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MediaFileReadDto {
    private UUID id;
    private String nomOriginal;
    private String typeMedia;
    private Long tailleFichier;
    private String typeMime;
    private String hashSha256;
    private String urlAcces;
    private String legende;
    private String altText;
    private LocalDateTime dateCreation;
    private Boolean isExisting; // Indique si le fichier existait déjà
}