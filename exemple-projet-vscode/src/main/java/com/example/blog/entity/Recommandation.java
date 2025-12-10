package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.RecommandationStatus;

@Entity
@Table(name = "recommandations")
public class Recommandation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "score_ia")
    private Float scoreIa;

    @Column(name = "raison")
    private String raison;

    @Column(name = "date_generation")
    private LocalDateTime dateGeneration;

    @Column(name = "affiche")
    private Boolean affiche;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RecommandationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return affiche;
    }

    public RecommandationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getAffiche() {
        return affiche;
    }
    public void setAffiche(Boolean affiche) {
        this.affiche = affiche;
    }
    public Integer getArticleId() {
        return articleId;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }
    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRaison() {
        return raison;
    }
    public void setRaison(String raison) {
        this.raison = raison;
    }
    public Float getScoreIa() {
        return scoreIa;
    }
    public void setScoreIa(Float scoreIa) {
        this.scoreIa = scoreIa;
    }
    public void setStatus(RecommandationStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void generer() {
        // TODO: Implement generer logic
    }

    public void enregistrerClic() {
        // TODO: Implement enregistrerClic logic
    }

    public void recalculer() {
        // TODO: Implement recalculer logic
    }

    public void suspend() {
        if (this.status != RecommandationStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = RecommandationStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != RecommandationStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = RecommandationStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
