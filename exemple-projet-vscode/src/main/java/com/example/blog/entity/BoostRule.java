package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.BoostRuleStatus;

@Entity
@Table(name = "boostrules")
public class BoostRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "seuil_min")
    private Integer seuilMin;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "region_pays_id")
    private Integer regionPaysId;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BoostRuleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return active;
    }

    public BoostRuleStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRegionPaysId() {
        return regionPaysId;
    }
    public void setRegionPaysId(Integer regionPaysId) {
        this.regionPaysId = regionPaysId;
    }
    public Integer getSeuilMin() {
        return seuilMin;
    }
    public void setSeuilMin(Integer seuilMin) {
        this.seuilMin = seuilMin;
    }
    public void setStatus(BoostRuleStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean evaluerArticle(String article) {
        // TODO: Implement evaluerArticle logic
        return null;
    }

    public void appliquerBoost() {
        // TODO: Implement appliquerBoost logic
    }

    public void suspend() {
        if (this.status != BoostRuleStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = BoostRuleStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != BoostRuleStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = BoostRuleStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
