package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.RegionStatus;

@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom_pays")
    private String nomPays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RegionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RegionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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
    public String getNomPays() {
        return nomPays;
    }
    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }
    public void setStatus(RegionStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.util.List<Article> obtenirArticlesRegion() {
        // TODO: Implement obtenirArticlesRegion logic
        return null;
    }

    public String obtenirStatistiques() {
        // TODO: Implement obtenirStatistiques logic
        return null;
    }

    public void suspend() {
        if (this.status != RegionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = RegionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != RegionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = RegionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
