package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.HomepageLayoutStatus;

@Entity
@Table(name = "homepagelayouts")
public class HomepageLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "items_per_page")
    private Integer itemsPerPage;

    @Column(name = "config_json", columnDefinition = "JSON")
    private String configJson;

    @Column(name = "actif")
    private Boolean actif;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HomepageLayoutStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return actif;
    }

    public HomepageLayoutStatus getStatus() {
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
    public String getConfigJson() {
        return configJson;
    }
    public void setConfigJson(String configJson) {
        this.configJson = configJson;
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
    public Integer getItemsPerPage() {
        return itemsPerPage;
    }
    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
    public void setStatus(HomepageLayoutStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void appliquerLayout() {
        // TODO: Implement appliquerLayout logic
    }

    public String previsualiser() {
        // TODO: Implement previsualiser logic
        return null;
    }

    public void suspend() {
        if (this.status != HomepageLayoutStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = HomepageLayoutStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != HomepageLayoutStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = HomepageLayoutStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
