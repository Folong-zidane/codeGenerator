package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.LienPartageStatus;

@Entity
@Table(name = "lienpartages")
public class LienPartage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "code_unique")
    private String codeUnique;

    @Column(name = "clics")
    private Integer clics;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LienPartageStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LienPartageStatus getStatus() {
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
    public Integer getClics() {
        return clics;
    }
    public void setClics(Integer clics) {
        this.clics = clics;
    }
    public String getCodeUnique() {
        return codeUnique;
    }
    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(LienPartageStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String genererLien() {
        // TODO: Implement genererLien logic
        return "";
    }

    public void tracker() {
        // TODO: Implement tracker logic
    }

    public String analyserPerformance() {
        // TODO: Implement analyserPerformance logic
        return null;
    }

    public void suspend() {
        if (this.status != LienPartageStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = LienPartageStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != LienPartageStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = LienPartageStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
