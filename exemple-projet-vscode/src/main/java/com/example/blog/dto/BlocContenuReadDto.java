package com.example.blog.dto;

import lombok.Data;

@Data
public class BlocContenuReadDto {
    private Integer id;
    private String typeBloc;
    private String contenuTexte;
    private String contenuMarkdown;
    private String mediaFileUrl;
    private String embedUrl;
    private String legende;
    private String altText;
    private String position;
    private Integer ordre;
    private String codeLanguage;
}
