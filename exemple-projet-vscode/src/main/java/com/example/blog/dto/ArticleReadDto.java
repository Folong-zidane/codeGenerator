package com.example.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleReadDto {
    private Integer id;
    private String titre;
    private String slug;
    private String description;
    private String rubriqueNom;
    private String auteurNom;
    private String imageCouvertureUrl;
    private List<BlocContenuReadDto> blocsContenu;
    private List<String> tags;
    private String statut;
    private LocalDateTime datePublication;
    private LocalDateTime dateCreation;
    private Integer vues;
    private Integer telechargements;
    private Integer partages;
    private Integer commentaires;
    private String region;
}
