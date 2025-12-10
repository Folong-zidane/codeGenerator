package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.TraductionStatus;

@Entity
@Table(name = "traductions")
public class Traduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "titre_traduit")
    private String titreTraduit;

    @Column(name = "contenu_traduit", columnDefinition = "JSON")
    private String contenuTraduit;

    @Column(name = "progress")
    private Integer progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TraductionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TraductionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Integer getArticleId() {
        return articleId;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public String getContenuTraduit() {
        return contenuTraduit;
    }
    public void setContenuTraduit(String contenuTraduit) {
        this.contenuTraduit = contenuTraduit;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getProgress() {
        return progress;
    }
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    public void setStatus(TraductionStatus status) {
        this.status = status;
    }
    public String getTitreTraduit() {
        return titreTraduit;
    }
    public void setTitreTraduit(String titreTraduit) {
        this.titreTraduit = titreTraduit;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void traduire() {
        // TODO: Implement traduire logic
    }

    public void valider() {
        // TODO: Implement valider logic
    }

    public void synchroniser() {
        // TODO: Implement synchroniser logic
    }

    public void suspend() {
        if (this.status != TraductionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = TraductionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != TraductionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = TraductionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
