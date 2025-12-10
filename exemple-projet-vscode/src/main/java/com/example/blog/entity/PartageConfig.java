package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PartageConfigStatus;

@Entity
@Table(name = "partageconfigs")
public class PartageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "url_courte")
    private String urlCourte;

    @Column(name = "partages_total")
    private Integer partagesTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PartageConfigStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PartageConfigStatus getStatus() {
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPartagesTotal() {
        return partagesTotal;
    }
    public void setPartagesTotal(Integer partagesTotal) {
        this.partagesTotal = partagesTotal;
    }
    public void setStatus(PartageConfigStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUrlCourte() {
        return urlCourte;
    }
    public void setUrlCourte(String urlCourte) {
        this.urlCourte = urlCourte;
    }

    public String genererLienCourt() {
        // TODO: Implement genererLienCourt logic
        return "";
    }

    public String genererQRCode() {
        // TODO: Implement genererQRCode logic
        return "";
    }

    public String genererMetasTags() {
        // TODO: Implement genererMetasTags logic
        return null;
    }

    public void incrementerPartages() {
        // TODO: Implement incrementerPartages logic
    }

    public void suspend() {
        if (this.status != PartageConfigStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PartageConfigStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PartageConfigStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PartageConfigStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
