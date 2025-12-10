package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ArticleStatus;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "image_couverture")
    private String imageCouverture;

    @Column(name = "slug")
    private String slug;

    @Column(name = "auteur_id")
    private Integer auteurId;

    @Column(name = "rubrique_id")
    private Integer rubriqueId;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    @Column(name = "statut")
    private String statut;

    @Column(name = "visible")
    private Boolean visible;

    @Column(name = "region")
    private String region;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "image_couverture_id")
    private Integer imageCouvertureId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public String getImageCouverture() {
        return imageCouverture;
    }

    public String getSlug() {
        return slug;
    }

    public Integer getAuteurId() {
        return auteurId;
    }

    public Integer getRubriqueId() {
        return rubriqueId;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setAuteurId(Integer auteurId) {
        this.auteurId = auteurId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setImageCouverture(String imageCouverture) {
        this.imageCouverture = imageCouverture;
    }
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
    public void setRubriqueId(Integer rubriqueId) {
        this.rubriqueId = rubriqueId;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public void setStatus(ArticleStatus status) {
        this.status = status;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public Boolean getVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Integer getImageCouvertureId() {
        return imageCouvertureId;
    }
    public void setImageCouvertureId(Integer imageCouvertureId) {
        this.imageCouvertureId = imageCouvertureId;
    }

    public void publier() {
        // TODO: Implement publier logic
    }

    public void archiver() {
        // TODO: Implement archiver logic
    }

    public void programmer(String date) {
        // TODO: Implement programmer logic
    }

    public BlocContenu ajouterBloc() {
        // TODO: Implement ajouterBloc logic
        return null;
    }

    public void modifierBloc(String blocId) {
        // TODO: Implement modifierBloc logic
    }

    public void supprimerBloc(String blocId) {
        // TODO: Implement supprimerBloc logic
    }

    public ContentVersion genererVersion() {
        // TODO: Implement genererVersion logic
        return null;
    }

    public void suspend() {
        if (this.status != ArticleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ArticleStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ArticleStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ArticleStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
