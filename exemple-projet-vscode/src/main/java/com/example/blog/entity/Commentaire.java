package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CommentaireStatus;

@Entity
@Table(name = "commentaires")
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "texte", columnDefinition = "TEXT")
    private String texte;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "spam_score")
    private Float spamScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentaireStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getTexte() {
        return texte;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public Integer getLikes() {
        return likes;
    }

    public Float getSpamScore() {
        return spamScore;
    }

    public CommentaireStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public void setSpamScore(Float spamScore) {
        this.spamScore = spamScore;
    }
    public void setStatus(CommentaireStatus status) {
        this.status = status;
    }
    public void setTexte(String texte) {
        this.texte = texte;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void moderer() {
        // TODO: Implement moderer logic
    }

    public Commentaire repondre() {
        // TODO: Implement repondre logic
        return null;
    }

    public void signaler() {
        // TODO: Implement signaler logic
    }

    public Boolean verifierSpam() {
        // TODO: Implement verifierSpam logic
        return null;
    }

    public void suspend() {
        if (this.status != CommentaireStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CommentaireStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CommentaireStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CommentaireStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
