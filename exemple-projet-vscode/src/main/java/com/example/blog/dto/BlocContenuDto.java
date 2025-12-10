package com.example.blog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class BlocContenuDto {
    
    @NotBlank(message = "Le type de bloc est obligatoire")
    private String typeBloc; // TEXTE, IMAGE, GALERIE, VIDEO, PDF, AUDIO, CODE, CITATION
    
    private String contenuTexte;
    private String contenuMarkdown;
    private Integer mediaFileId;
    private String embedUrl;
    private String legende;
    private String altText;
    private String position; // left, center, right
    private Integer ordre;
    private String codeLanguage;
}
