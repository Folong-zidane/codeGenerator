package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PaywallStatus;

@Entity
@Table(name = "paywalls")
public class Paywall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "prix_fcfa")
    private Integer prixFcfa;

    @Column(name = "description")
    private String description;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "preview_content", columnDefinition = "TEXT")
    private String previewContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaywallStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return actif;
    }



    public PaywallStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getActif() {
        return actif;
    }
    public void setActif(Boolean actif) {
        this.actif = actif;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPreviewContent() {
        return previewContent;
    }
    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }
    public Integer getPrixFcfa() {
        return prixFcfa;
    }
    public void setPrixFcfa(Integer prixFcfa) {
        this.prixFcfa = prixFcfa;
    }
    public void setStatus(PaywallStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean verifierAcces(String userId) {
        // TODO: Implement verifierAcces logic
        return null;
    }

    public void activer() {
        // TODO: Implement activer logic
    }

    public void desactiver() {
        // TODO: Implement desactiver logic
    }

    public void suspend() {
        if (this.status != PaywallStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PaywallStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PaywallStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PaywallStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
