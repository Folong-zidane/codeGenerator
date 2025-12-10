package com.example.blog.dto;

import lombok.Data;
import javax.validation.constraints.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleCreateDto {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 10, max = 200, message = "Le titre doit contenir entre 10 et 200 caractères")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    @Size(min = 50, max = 500, message = "La description doit contenir entre 50 et 500 caractères")
    private String description;
    
    @NotNull(message = "La rubrique est obligatoire")
    @Min(value = 1, message = "L'ID de rubrique doit être positif")
    private Integer rubriqueId;
    
    private Integer auteurId;
    
    private Integer imageCouvertureId;
    
    @NotEmpty(message = "L'article doit contenir au moins un bloc de contenu")
    @Size(min = 1, max = 50, message = "Un article peut contenir entre 1 et 50 blocs de contenu")
    private List<BlocContenuDto> blocsContenu;
    
    private List<Integer> tagIds;
    
    private String statut; // BROUILLON, PUBLIE, ARCHIVE
    
    private LocalDateTime datePublication;
    
    private Boolean visible = true;
    
    private String region;
}
