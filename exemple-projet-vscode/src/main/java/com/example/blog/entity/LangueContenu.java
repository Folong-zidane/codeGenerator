package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.LangueContenuStatus;

@Entity
@Table(name = "languecontenus")
public class LangueContenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "auto_traduit")
    private Boolean autoTraduit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LangueContenuStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return autoTraduit;
    }

    public LangueContenuStatus getStatus() {
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
    public Boolean getAutoTraduit() {
        return autoTraduit;
    }
    public void setAutoTraduit(Boolean autoTraduit) {
        this.autoTraduit = autoTraduit;
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
    public void setStatus(LangueContenuStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String detecterLangue() {
        // TODO: Implement detecterLangue logic
        return "";
    }

    public void traduireAuto() {
        // TODO: Implement traduireAuto logic
    }

    public void suspend() {
        if (this.status != LangueContenuStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = LangueContenuStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != LangueContenuStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = LangueContenuStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
