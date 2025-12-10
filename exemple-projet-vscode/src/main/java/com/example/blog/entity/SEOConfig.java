package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.SEOConfigStatus;

@Entity
@Table(name = "seoconfigs")
public class SEOConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "schema_json_ld", columnDefinition = "JSON")
    private String schemaJsonLd;

    @Column(name = "robots_index")
    private Boolean robotsIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SEOConfigStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return robotsIndex;
    }

    public SEOConfigStatus getStatus() {
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
    public String getMetaTitle() {
        return metaTitle;
    }
    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }
    public Boolean getRobotsIndex() {
        return robotsIndex;
    }
    public void setRobotsIndex(Boolean robotsIndex) {
        this.robotsIndex = robotsIndex;
    }
    public String getSchemaJsonLd() {
        return schemaJsonLd;
    }
    public void setSchemaJsonLd(String schemaJsonLd) {
        this.schemaJsonLd = schemaJsonLd;
    }
    public void setStatus(SEOConfigStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void genererAutomatic() {
        // TODO: Implement genererAutomatic logic
    }

    public String validerSEO() {
        // TODO: Implement validerSEO logic
        return null;
    }

    public String genererSchemaMarkup() {
        // TODO: Implement genererSchemaMarkup logic
        return null;
    }

    public void suspend() {
        if (this.status != SEOConfigStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = SEOConfigStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != SEOConfigStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = SEOConfigStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
