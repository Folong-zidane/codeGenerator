package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CategoryFilterStatus;

@Entity
@Table(name = "categoryfilters")
public class CategoryFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "categorie_id")
    private Integer categorieId;

    @Column(name = "result_limit")
    private Integer resultLimit;

    @Column(name = "filtres_json", columnDefinition = "JSON")
    private String filtresJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryFilterStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CategoryFilterStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getFiltresJson() {
        return filtresJson;
    }
    public void setFiltresJson(String filtresJson) {
        this.filtresJson = filtresJson;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getResultLimit() {
        return resultLimit;
    }
    public void setResultLimit(Integer resultLimit) {
        this.resultLimit = resultLimit;
    }
    public void setStatus(CategoryFilterStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.util.List<Article> appliquerFiltre() {
        // TODO: Implement appliquerFiltre logic
        return null;
    }

    public void suspend() {
        if (this.status != CategoryFilterStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CategoryFilterStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CategoryFilterStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CategoryFilterStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
