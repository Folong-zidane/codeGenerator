package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PartageArticleStatus;

@Entity
@Table(name = "partagearticles")
public class PartageArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "date_partage")
    private LocalDateTime datePartage;

    @Column(name = "clicks_count")
    private Integer clicksCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PartageArticleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PartageArticleStatus getStatus() {
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
    public Integer getClicksCount() {
        return clicksCount;
    }
    public void setClicksCount(Integer clicksCount) {
        this.clicksCount = clicksCount;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDatePartage() {
        return datePartage;
    }
    public void setDatePartage(LocalDateTime datePartage) {
        this.datePartage = datePartage;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(PartageArticleStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void enregistrerPartage() {
        // TODO: Implement enregistrerPartage logic
    }

    public void incrementerClicks() {
        // TODO: Implement incrementerClicks logic
    }

    public void suspend() {
        if (this.status != PartageArticleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PartageArticleStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PartageArticleStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PartageArticleStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
