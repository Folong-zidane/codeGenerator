package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.VueArticleStatus;

@Entity
@Table(name = "vuearticles")
public class VueArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "duree_lecture")
    private Integer dureeLecture;

    @Column(name = "date_vue")
    private LocalDateTime dateVue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VueArticleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VueArticleStatus getStatus() {
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
    public LocalDateTime getDateVue() {
        return dateVue;
    }
    public void setDateVue(LocalDateTime dateVue) {
        this.dateVue = dateVue;
    }
    public Integer getDureeLecture() {
        return dureeLecture;
    }
    public void setDureeLecture(Integer dureeLecture) {
        this.dureeLecture = dureeLecture;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void setStatus(VueArticleStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void enregistrerVue() {
        // TODO: Implement enregistrerVue logic
    }

    public Boolean verifierUnique() {
        // TODO: Implement verifierUnique logic
        return null;
    }

    public void suspend() {
        if (this.status != VueArticleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = VueArticleStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != VueArticleStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = VueArticleStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
