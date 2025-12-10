package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.FavoriStatus;

@Entity
@Table(name = "favoris")
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "collection")
    private String collection;

    @Column(name = "notes_privees", columnDefinition = "TEXT")
    private String notesPrivees;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FavoriStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public String getCollection() {
        return collection;
    }

    public String getNotesPrivees() {
        return notesPrivees;
    }

    public FavoriStatus getStatus() {
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
    public void setCollection(String collection) {
        this.collection = collection;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setNotesPrivees(String notesPrivees) {
        this.notesPrivees = notesPrivees;
    }
    public void setStatus(FavoriStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void ajouterCollection(String nom) {
        // TODO: Implement ajouterCollection logic
    }

    public void retirer() {
        // TODO: Implement retirer logic
    }

    public void suspend() {
        if (this.status != FavoriStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = FavoriStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != FavoriStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = FavoriStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
