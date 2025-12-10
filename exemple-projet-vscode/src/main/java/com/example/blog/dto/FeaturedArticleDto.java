package com.example.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeaturedArticleDto {
    private Integer articleId;
    private Integer position;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String section; // UNE, SIDEBAR, CAROUSEL
    private Boolean actif;
}