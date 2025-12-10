package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AdministrateurStatus;

@Entity
@Table(name = "administrateurs")
public class Administrateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "droits_complets")
    private Boolean droitsComplets;

    @Column(name = "niveau_acces")
    private Integer niveauAcces;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AdministrateurStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public Boolean getDroitsComplets() {
        return droitsComplets;
    }

    public Integer getNiveauAcces() {
        return niveauAcces;
    }

    public AdministrateurStatus getStatus() {
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
    public void setDroitsComplets(Boolean droitsComplets) {
        this.droitsComplets = droitsComplets;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setNiveauAcces(Integer niveauAcces) {
        this.niveauAcces = niveauAcces;
    }
    public void setStatus(AdministrateurStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void gererRubriques() {
        // TODO: Implement gererRubriques logic
    }

    public Statistiques consulterStatistiques() {
        // TODO: Implement consulterStatistiques logic
        return null;
    }

    public void gererUtilisateurs() {
        // TODO: Implement gererUtilisateurs logic
    }

    public void gererParametres() {
        // TODO: Implement gererParametres logic
    }

    public void modererCommentaires() {
        // TODO: Implement modererCommentaires logic
    }

    public java.io.File exporterDonnees() {
        // TODO: Implement exporterDonnees logic
        return null;
    }

    public void suspend() {
        if (this.status != AdministrateurStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AdministrateurStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AdministrateurStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AdministrateurStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
