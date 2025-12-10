package com.example.blog.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleUpdateDto {
    
    @Size(min = 10, max = 200, message = "Le titre doit contenir entre 10 et 200 caractères")
    private String titre;
    
    @Size(min = 50, max = 500, message = "La description doit contenir entre 50 et 500 caractères")
    private String description;
    
    private Integer rubriqueId;
    
    private Integer imageCouvertureId;
    
    private List<BlocContenuDto> blocsContenu;
    
    private List<Integer> tagIds;
    
    private String statut;
    
    private LocalDateTime datePublication;
    
    private Boolean visible;
    
    private String region;
}
