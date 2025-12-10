package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.BlocContenuStatus;

@Entity
@Table(name = "bloccontenus")
public class BlocContenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "contenu", columnDefinition = "JSON")
    private String contenu;

    @Column(name = "ordre_position")
    private Integer ordrePosition;

    @Column(name = "niveau_hierarchie")
    private Integer niveauHierarchie;

    @Column(name = "parent_bloc_id")
    private Integer parentBlocId;

    @Column(name = "config_affichage", columnDefinition = "JSON")
    private String configAffichage;

    @Column(name = "media_id")
    private Integer mediaId;

    @Column(name = "type_bloc")
    private String typeBloc;

    @Column(name = "contenu_texte", columnDefinition = "TEXT")
    private String contenuTexte;

    @Column(name = "contenu_markdown", columnDefinition = "TEXT")
    private String contenuMarkdown;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "embed_url")
    private String embedUrl;

    @Column(name = "legende")
    private String legende;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "position")
    private String position;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "code_language")
    private String codeLanguage;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BlocContenuStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public String getContenu() {
        return contenu;
    }

    public Integer getOrdrePosition() {
        return ordrePosition;
    }

    public Integer getNiveauHierarchie() {
        return niveauHierarchie;
    }

    public Integer getParentBlocId() {
        return parentBlocId;
    }

    public String getConfigAffichage() {
        return configAffichage;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public BlocContenuStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public void setConfigAffichage(String configAffichage) {
        this.configAffichage = configAffichage;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }
    public void setNiveauHierarchie(Integer niveauHierarchie) {
        this.niveauHierarchie = niveauHierarchie;
    }
    public void setOrdrePosition(Integer ordrePosition) {
        this.ordrePosition = ordrePosition;
    }
    public void setParentBlocId(Integer parentBlocId) {
        this.parentBlocId = parentBlocId;
    }
    public void setStatus(BlocContenuStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getTypeBloc() {
        return typeBloc;
    }
    public void setTypeBloc(String typeBloc) {
        this.typeBloc = typeBloc;
    }
    public String getContenuTexte() {
        return contenuTexte;
    }
    public void setContenuTexte(String contenuTexte) {
        this.contenuTexte = contenuTexte;
    }
    public String getContenuMarkdown() {
        return contenuMarkdown;
    }
    public void setContenuMarkdown(String contenuMarkdown) {
        this.contenuMarkdown = contenuMarkdown;
    }
    public Integer getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public String getEmbedUrl() {
        return embedUrl;
    }
    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }
    public String getLegende() {
        return legende;
    }
    public void setLegende(String legende) {
        this.legende = legende;
    }
    public String getAltText() {
        return altText;
    }
    public void setAltText(String altText) {
        this.altText = altText;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Integer getOrdre() {
        return ordre;
    }
    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
    public String getCodeLanguage() {
        return codeLanguage;
    }
    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void reordonner(String position) {
        // TODO: Implement reordonner logic
    }

    public String render() {
        // TODO: Implement render logic
        return null;
    }

    public BlocContenu ajouterSousBloc() {
        // TODO: Implement ajouterSousBloc logic
        return null;
    }

    public BlocContenu dupliquer() {
        // TODO: Implement dupliquer logic
        return null;
    }

    public void suspend() {
        if (this.status != BlocContenuStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = BlocContenuStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != BlocContenuStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = BlocContenuStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
