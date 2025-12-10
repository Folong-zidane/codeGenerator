package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.RubriqueStatus;

@Entity
@Table(name = "rubriques")
public class Rubrique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug")
    private String slug;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "icone")
    private String icone;

    @Column(name = "couleur")
    private String couleur;

    @Column(name = "visible")
    private Boolean visible;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RubriqueStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public String getIcone() {
        return icone;
    }

    public String getCouleur() {
        return couleur;
    }

    public Boolean getVisible() {
        return visible;
    }

    public RubriqueStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIcone(String icone) {
        this.icone = icone;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public void setStatus(RubriqueStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rubrique ajouterSousRubrique() {
        // TODO: Implement ajouterSousRubrique logic
        return null;
    }

    public void supprimerSousRubrique(String id) {
        // TODO: Implement supprimerSousRubrique logic
    }

    public java.util.List<Article> obtenirArticles() {
        // TODO: Implement obtenirArticles logic
        return null;
    }

    public Integer compterArticles() {
        // TODO: Implement compterArticles logic
        return 0;
    }

    public void suspend() {
        if (this.status != RubriqueStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = RubriqueStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != RubriqueStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = RubriqueStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
