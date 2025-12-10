package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CardPreviewStatus;

@Entity
@Table(name = "cardpreviews")
public class CardPreview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "fields_display", columnDefinition = "JSON")
    private String fieldsDisplay;

    @Column(name = "ordre")
    private Integer ordre;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardPreviewStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CardPreviewStatus getStatus() {
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
    public String getFieldsDisplay() {
        return fieldsDisplay;
    }
    public void setFieldsDisplay(String fieldsDisplay) {
        this.fieldsDisplay = fieldsDisplay;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getOrdre() {
        return ordre;
    }
    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
    public void setStatus(CardPreviewStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String render() {
        // TODO: Implement render logic
        return null;
    }

    public void suspend() {
        if (this.status != CardPreviewStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CardPreviewStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CardPreviewStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CardPreviewStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
