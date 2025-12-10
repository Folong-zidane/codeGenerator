package com.example.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticlePublicationDto {
    private LocalDateTime datePublication;
    private Boolean enAvantPremiere;
    private LocalDateTime dateFinAvantPremiere;
    private String motifRejet;
    private Boolean notifierAbonnes;
    private Boolean publierReseauxSociaux;
}