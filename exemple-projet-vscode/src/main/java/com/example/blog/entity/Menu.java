package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MenuStatus;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "items_json", columnDefinition = "JSON")
    private String itemsJson;

    @Column(name = "langue_id")
    private Integer langueId;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "actif")
    private Boolean actif;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return actif;
    }

    public MenuStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getActif() {
        return actif;
    }
    public void setActif(Boolean actif) {
        this.actif = actif;
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
    public String getItemsJson() {
        return itemsJson;
    }
    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }
    public Integer getLangueId() {
        return langueId;
    }
    public void setLangueId(Integer langueId) {
        this.langueId = langueId;
    }
    public Integer getOrdre() {
        return ordre;
    }
    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
    public void setStatus(MenuStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void ajouterItem() {
        // TODO: Implement ajouterItem logic
    }

    public void reordonner() {
        // TODO: Implement reordonner logic
    }

    public String render() {
        // TODO: Implement render logic
        return null;
    }

    public void suspend() {
        if (this.status != MenuStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MenuStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MenuStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MenuStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
