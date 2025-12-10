package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.StatistiquesStatus;

@Entity
@Table(name = "statistiqueses")
public class Statistiques {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "vues")
    private Integer vues;

    @Column(name = "taux_rebond")
    private Float tauxRebond;

    @Column(name = "date_maj")
    private LocalDateTime dateMaj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatistiquesStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public StatistiquesStatus getStatus() {
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
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateMaj() {
        return dateMaj;
    }
    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(StatistiquesStatus status) {
        this.status = status;
    }
    public Float getTauxRebond() {
        return tauxRebond;
    }
    public void setTauxRebond(Float tauxRebond) {
        this.tauxRebond = tauxRebond;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getVues() {
        return vues;
    }
    public void setVues(Integer vues) {
        this.vues = vues;
    }

    public void mettreAJour() {
        // TODO: Implement mettreAJour logic
    }

    public void incrementerVues() {
        // TODO: Implement incrementerVues logic
    }

    public void incrementerVuesUniques(String ip) {
        // TODO: Implement incrementerVuesUniques logic
    }

    public void incrementerTelechargements() {
        // TODO: Implement incrementerTelechargements logic
    }

    public void incrementerPartages() {
        // TODO: Implement incrementerPartages logic
    }

    public Float calculerTauxEngagement() {
        // TODO: Implement calculerTauxEngagement logic
        return 0.0f;
    }

    public Float calculerScoreBoost() {
        // TODO: Implement calculerScoreBoost logic
        return 0.0f;
    }

    public void suspend() {
        if (this.status != StatistiquesStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = StatistiquesStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != StatistiquesStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = StatistiquesStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
