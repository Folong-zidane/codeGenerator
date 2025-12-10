package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.UserPreferenceStatus;

@Entity
@Table(name = "userpreferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "categories_favorites", columnDefinition = "JSON")
    private String categoriesFavorites;

    @Column(name = "langue_pref")
    private String languePref;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserPreferenceStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPreferenceStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getCategoriesFavorites() {
        return categoriesFavorites;
    }
    public void setCategoriesFavorites(String categoriesFavorites) {
        this.categoriesFavorites = categoriesFavorites;
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
    public String getLanguePref() {
        return languePref;
    }
    public void setLanguePref(String languePref) {
        this.languePref = languePref;
    }
    public void setStatus(UserPreferenceStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String personnaliserHomepage() {
        // TODO: Implement personnaliserHomepage logic
        return null;
    }

    public void suspend() {
        if (this.status != UserPreferenceStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = UserPreferenceStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != UserPreferenceStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = UserPreferenceStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
